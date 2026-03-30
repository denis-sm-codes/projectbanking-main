package com.petprojects.projectbanking.service;

import com.petprojects.projectbanking.dto.request.DtoLogin;
import com.petprojects.projectbanking.dto.response.DtoAuthResponse;
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

    public DtoAuthResponse login(DtoLogin dto) {

        User user = userRepository.findByUserNumber(dto.getUserNumber())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        return jwtUtil.generateFullAuthResponse(user.getUserNumber(), user.getRole().name());
    }

    @Transactional
    public void logout(String token) {
        // Отрезаем "Bearer ", если он пришел в заголовке
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;

        // Извлекаем номер пользователя через JwtUtil
        // (Проверь, как называется метод в твоем JwtUtil: getUsername или extractUsername)
        String userNumber = jwtUtil.getUserNumber(jwt);

        // Удаляем рефреш-токен из базы, чтобы сессия закрылась
        refreshTokenRepository.deleteByUserNumber(userNumber);
    }


}