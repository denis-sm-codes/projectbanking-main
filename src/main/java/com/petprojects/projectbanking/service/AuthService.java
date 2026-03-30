package com.petprojects.projectbanking.service;

import com.petprojects.projectbanking.dto.request.DtoLogin;
import com.petprojects.projectbanking.model.User;
import com.petprojects.projectbanking.repository.UserRepository;
import com.petprojects.projectbanking.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public String login(DtoLogin dto) {

        User user = userRepository.findByUserNumber(dto.getUserNumber())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        return jwtUtil.generateAccessToken(user.getUserNumber(), user.getRole().name());
    }
}