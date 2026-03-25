package com.petprojects.projectbanking.security;

import com.petprojects.projectbanking.model.RefreshToken;
import com.petprojects.projectbanking.repository.RefreshTokenRepository;
import com.petprojects.projectbanking.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

@Component
@AllArgsConstructor
@Getter
@Setter
public class JwtUtil {
    private UserRepository userRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    public String generateAccessToken(String userNumber, String role) {
        return Jwts.builder()
                .claim("userNumber", userNumber)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessExpiration()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();
    }
    public String generateRefreshToken(String userNumber) {
        String refreshToken = Jwts.builder()
                .claim("userNumber", userNumber)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpiration()))
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .compact();

        refreshTokenRepository.findByUserNumber(userNumber)
                .ifPresent(old -> refreshTokenRepository.delete(old));

        RefreshToken refreshTokenNew = new RefreshToken();
        refreshTokenNew.setToken(refreshToken);
        refreshTokenNew.setUserNumber(userNumber);

        refreshTokenNew.setExpiryDate(Instant.ofEpochMilli(System.currentTimeMillis() + jwtProperties.getRefreshExpiration())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());

        refreshTokenNew.setRevoked(false);
        refreshTokenRepository.save(refreshTokenNew);

        return refreshToken;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token).getBody();
    }

    public String getUserNumber(String token) {
        return getClaims(token).get("userNumber", String.class);
    }
    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }
}