package com.petprojects.projectbanking.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Включаем CORS с нашей конфигурацией
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Отключаем CSRF (для REST API с JWT это стандарт)
                .csrf(AbstractHttpConfigurer::disable)

                // 3. Настраиваем правила доступа
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/h2-console/**").permitAll()
                        .requestMatchers("/auth/**").permitAll() // Разрешаем логин всем
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/support/**").hasAnyRole("SUPPORT", "ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN", "SUPPORT")
                        .anyRequest().authenticated()
                )

                // 4. Добавляем наш JWT фильтр перед стандартным
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Разрешаем запросы с любого адреса (включая локальные файлы)
        config.setAllowedOriginPatterns(List.of("*"));

        // Разрешаем все стандартные методы
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Разрешаем любые заголовки (Authorization, Content-Type и т.д.)
        config.setAllowedHeaders(List.of("*"));

        // ВАЖНО: При использовании "*" для OriginPatterns, AllowCredentials должно быть false
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}