package com.petprojects.projectbanking.security;

import com.petprojects.projectbanking.dto.response.DtoAuthResponse;
import com.petprojects.projectbanking.model.RefreshToken;
import com.petprojects.projectbanking.model.User;
import com.petprojects.projectbanking.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Getter
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private Key signingKey;

    @PostConstruct
    public void init() {
        // Безопасная инициализация ключа из свойств
        if (jwtProperties.getSecret() == null || jwtProperties.getSecret().length() < 32) {
            this.signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } else {
            byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.getSecret());
            this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        }
    }

    // 1. Генерируем Access Token
    public String generateAccessToken(String userNumber, String role) {
        return Jwts.builder()
                .setSubject(userNumber)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessExpiration()))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 2. Генерируем Refresh Token и СРАЗУ сохраняем в БД
    public String generateRefreshToken(String userNumber) {
        String token = Jwts.builder()
                .setSubject(userNumber)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpiration()))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        // Чистим старые токены пользователя перед сохранением нового
        refreshTokenRepository.findByUserNumber(userNumber)
                .ifPresent(refreshTokenRepository::delete);

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(token);
        refreshTokenEntity.setUserNumber(userNumber);
        refreshTokenEntity.setExpiryDate(LocalDateTime.now().plusNanos(jwtProperties.getRefreshExpiration() * 1_000_000));
        refreshTokenEntity.setRevoked(false);

        refreshTokenRepository.save(refreshTokenEntity);
        return token;
    }

    // 3. Удобный метод для получения полной пары токенов
    public DtoAuthResponse generateFullAuthResponse(String userNumber, String role) {
        return new DtoAuthResponse(
                generateAccessToken(userNumber, role),
                generateRefreshToken(userNumber)
        );
    }

    // Валидация
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
    }

    public String getUserNumber(String token) {
        return getClaims(token).getSubject();
    }
}