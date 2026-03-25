package com.petprojects.projectbanking.repository;

import com.petprojects.projectbanking.model.Transaction;
import com.petprojects.projectbanking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderAccount(Account senderAccount);

    List<Transaction> findByReceiverAccount(Account receiverAccount);

    List<Transaction> findBySenderAccountOrReceiverAccount(Account sender, Account receiver);
}
