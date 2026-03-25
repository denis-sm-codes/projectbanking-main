package com.petprojects.projectbanking.service;

import com.petprojects.projectbanking.dto.response.DtoAuthResponse;
import com.petprojects.projectbanking.model.RefreshToken;
import com.petprojects.projectbanking.model.User;
import com.petprojects.projectbanking.repository.RefreshTokenRepository;
import com.petprojects.projectbanking.repository.UserRepository;
import com.petprojects.projectbanking.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public DtoAuthResponse refresh(String refreshToken) {
        RefreshToken stored = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        if (stored.isRevoked() || stored.isExpired()) {
            throw new RuntimeException("Invalid refresh token");
        }

        refreshTokenRepository.delete(stored);

        User user = userRepository.findByUserNumber(stored.getUserNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newRefresh = jwtUtil.generateRefreshToken(user.getUserNumber());
        saveRefreshTokenToDatabase(newRefresh, user.getUserNumber());

        String newAccess = jwtUtil.generateAccessToken(user.getUserNumber(), String.valueOf(user.getRole()));

        return new DtoAuthResponse(newAccess, newRefresh);
    }

    private void saveRefreshTokenToDatabase(String token, String userNumber) {
        long expirationMillis = jwtUtil.getJwtProperties().getRefreshExpiration();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUserNumber(userNumber);

        refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(expirationMillis / 1000));

        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);
    }
}