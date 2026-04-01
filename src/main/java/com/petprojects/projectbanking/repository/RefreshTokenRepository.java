package com.petprojects.projectbanking.repository;

import com.petprojects.projectbanking.model.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserNumber(String userNumber);

    @Modifying
    @Transactional
    void deleteByUserNumber(String userNumber);
}