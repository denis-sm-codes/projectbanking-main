package com.petprojects.projectbanking.repository;

import com.petprojects.projectbanking.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserNumber(String userNumber);

    @Modifying
    void deleteByUserNumber(String userNumber);
}