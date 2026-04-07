package com.petprojects.projectbanking.service;

import com.petprojects.projectbanking.dto.request.DtoLogin;
import com.petprojects.projectbanking.dto.response.DtoAuthResponse;
import com.petprojects.projectbanking.exception.AccountNotFoundException;
import com.petprojects.projectbanking.exception.UserNotFoundException;
import com.petprojects.projectbanking.model.Account;
import com.petprojects.projectbanking.model.User;
import com.petprojects.projectbanking.repository.RefreshTokenRepository;
import com.petprojects.projectbanking.repository.UserRepository;
import com.petprojects.projectbanking.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public DtoAuthResponse login(DtoLogin dto) {
        User user = userRepository.findByUserNumber(dto.getUserNumber())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserNumber()));

        Account account = user.getAccounts().stream()
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException(user.getUserNumber()));

        return jwtUtil.generateFullAuthResponse(
                user.getUserNumber(),
                user.getRole().name(),
                account.getCountNumber()
        );
    }

    @Transactional
    public void logout(String token) {
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;
        String userNumber = jwtUtil.getUserNumber(jwt);
        refreshTokenRepository.deleteByUserNumber(userNumber);
    }
}