package com.petprojects.projectbanking.repository;

import com.petprojects.projectbanking.model.Transaction;
import com.petprojects.projectbanking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderAccount(Account senderAccount);

    List<Transaction> findByReceiverAccount(Account receiverAccount);

    @Query("SELECT t FROM Transaction t " +
            "JOIN FETCH t.senderAccount " +
            "JOIN FETCH t.receiverAccount " +
            "WHERE t.senderAccount = :account OR t.receiverAccount = :account")
    List<Transaction> findBySenderAccountOrReceiverAccount(@Param("account") Account account);
}