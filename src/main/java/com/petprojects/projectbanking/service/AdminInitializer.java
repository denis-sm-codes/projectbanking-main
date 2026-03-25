package com.petprojects.projectbanking.service;

import com.petprojects.projectbanking.model.Role;
import com.petprojects.projectbanking.model.User;
import com.petprojects.projectbanking.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;

    @PostConstruct
    public void initAdmin() {
        // Проверяем, есть ли уже админ с email "admin@bank.com"
        boolean adminExists = userRepository.existsByEmail("admin@bank.com");
        if (!adminExists) {
            User admin = User.builder()
                    .firstname("Super")
                    .secondname("Admin")
                    .email("admin@bank.com")
                    .role(Role.ADMIN)
                    .enabled(true)
                    .userNumber("0000001") // можно вручную или через генератор
                    .build();

            userRepository.save(admin);
            System.out.println("✅ Admin user created: admin@bank.com / userNumber: 0000001");
        }
    }
}