package com.petprojects.projectbanking.service;

import com.petprojects.projectbanking.dto.request.DtoLogin;
import com.petprojects.projectbanking.model.User;
import com.petprojects.projectbanking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    /**
     * Логин пользователя по userNumber
     * @param dto Данные для логина (userNumber)
     * @return JWT токен
     */
    public String login(DtoLogin dto) {
        // Находим пользователя по userNumber
        User user = userRepository.findByUserNumber(dto.getUserNumber())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // Генерируем JWT токен
        return jwtService.generateToken(user.getUserNumber(), user.getRole().name());
    }
}