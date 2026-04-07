package com.petprojects.projectbanking.repository;

import com.petprojects.projectbanking.model.User;
import com.petprojects.projectbanking.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserNumber(String userNumber);

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    boolean existsByUserNumber(String userNumber);

    boolean existsByEmail(String email);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.accounts")
    List<User> findAllWithAccounts();
}