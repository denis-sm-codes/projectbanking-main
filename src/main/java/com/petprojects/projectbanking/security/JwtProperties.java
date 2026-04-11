package com.petprojects.projectbanking.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@ConfigurationProperties(prefix = "jwt")
@Component
@Getter
@Setter
public class JwtProperties {
    private String secret;
    private Duration accessExpiration;
    private Duration refreshExpiration;

    public long getAccessExpirationMillis() {
        return accessExpiration.toMillis();
    }

    public long getRefreshExpirationMillis() {
        return refreshExpiration.toMillis();
    }
}