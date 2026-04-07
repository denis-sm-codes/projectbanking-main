package com.petprojects.projectbanking.repository;

import com.petprojects.projectbanking.model.Account;
import com.petprojects.projectbanking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByCountNumber(String countNumber);

    Optional<Account> findByUser_UserNumber(String userNumber);

    Optional<Account> findByUser(User user);
}