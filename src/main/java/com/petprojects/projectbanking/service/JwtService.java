package com.petprojects.projectbanking.service;

import com.petprojects.projectbanking.security.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private Key signingKey;

    // Генерируем ключ после создания бина
    @PostConstruct
    private void init() {
        if (jwtProperties.getSecret() == null || jwtProperties.getSecret().length() < 32) {
            signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } else {
            // Если секрет в YAML — предполагаем Base64
            signingKey = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(jwtProperties.getSecret()));
        }
    }

    // Генерация токена по userNumber + роли
    public String generateToken(String userNumber, String role) {
        return Jwts.builder()
                .setSubject(userNumber)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessExpiration()))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Извлечение userNumber из токена
    public String extractUserNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Проверка валидности токена
    public boolean isTokenValid(String token, String userNumber) {
        final String extractedUserNumber = extractUserNumber(token);
        return extractedUserNumber.equals(userNumber) && !isTokenExpired(token);
    }

    // Проверка срока действия
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Общий метод извлечения claim
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Разбор всех claims из токена
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}