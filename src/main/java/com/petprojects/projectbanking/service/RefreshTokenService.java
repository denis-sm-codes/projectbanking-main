package com.petprojects.projectbanking.service;

import com.petprojects.projectbanking.dto.response.DtoAuthResponse;
import com.petprojects.projectbanking.model.RefreshToken;
import com.petprojects.projectbanking.model.User;
import com.petprojects.projectbanking.repository.AccountRepository;
import com.petprojects.projectbanking.repository.RefreshTokenRepository;
import com.petprojects.projectbanking.repository.UserRepository;
import com.petprojects.projectbanking.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository; // Добавлено, чтобы не горело красным

    @Transactional
    public DtoAuthResponse refresh(String refreshToken) {
        RefreshToken stored = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        if (stored.isRevoked() || stored.isExpired()) {
            refreshTokenRepository.delete(stored);
            throw new RuntimeException("Invalid refresh token");
        }

        User user = userRepository.findByUserNumber(stored.getUserNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String countNumber = accountRepository.findByUser_UserNumber(user.getUserNumber())
                .map(acc -> acc.getCountNumber())
                .orElse("NO_ACCOUNT");

        refreshTokenRepository.delete(stored);

        String newAccess = jwtUtil.generateAccessToken(
                user.getUserNumber(),
                user.getRole().name(),
                countNumber
        );

        String newRefresh = jwtUtil.generateRefreshToken(
                user.getUserNumber(),
                countNumber
        );

        return DtoAuthResponse.builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh)
                .build();
    }
}