package com.petprojects.projectbanking.security;

import com.petprojects.projectbanking.dto.response.DtoAuthResponse;
import com.petprojects.projectbanking.model.RefreshToken;
import com.petprojects.projectbanking.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
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
        String secret = jwtProperties.getSecret();
        if (secret == null || secret.length() < 32) {
            this.signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } else {
            this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        }
    }

    public String generateAccessToken(String userNumber, String role, String countNumber) {
        return Jwts.builder()
                .setSubject(userNumber)
                .claim("role", role)
                .claim("countNumber", countNumber)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessExpirationMillis()))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String userNumber, String countNumber) {
        String token = Jwts.builder()
                .setSubject(userNumber)
                .claim("countNumber", countNumber)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExpirationMillis()))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        refreshTokenRepository.findByUserNumber(userNumber)
                .ifPresent(refreshTokenRepository::delete);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .token(token)
                .userNumber(userNumber)
                .expiryDate(LocalDateTime.now().plus(jwtProperties.getRefreshExpiration()))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
        return token;
    }

    public DtoAuthResponse generateFullAuthResponse(String userNumber, String role, String countNumber) {
        return DtoAuthResponse.builder()
                .accessToken(generateAccessToken(userNumber, role, countNumber))
                .refreshToken(generateRefreshToken(userNumber, countNumber))
                .build();
    }

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

    public long getRefreshExpirationTime() {
        return jwtProperties.getRefreshExpirationMillis();
    }
}