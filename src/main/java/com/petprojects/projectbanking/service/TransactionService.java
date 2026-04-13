package com.petprojects.projectbanking.service;

import com.petprojects.projectbanking.dto.request.DtoTransaction;
import com.petprojects.projectbanking.dto.response.DtoPersonalTransact;
import com.petprojects.projectbanking.exception.AccountNotFoundException;
import com.petprojects.projectbanking.exception.TransactionException;
import com.petprojects.projectbanking.model.Account;
import com.petprojects.projectbanking.model.Transaction;
import com.petprojects.projectbanking.repository.AccountRepository;
import com.petprojects.projectbanking.repository.TransactionRepository;
import com.petprojects.projectbanking.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public DtoPersonalTransact makeTransaction(DtoTransaction dto) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Account sender = accountRepository.findByCountNumber(principal.countNumber())
                .orElseThrow(() -> new AccountNotFoundException("Ваш счет не найден"));

        Account receiver = accountRepository.findByCountNumber(dto.getToAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Счет получателя не найден: " + dto.getToAccountNumber()));


        if (sender.getStatus() != com.petprojects.projectbanking.model.AccountStatus.ACTIVE) {
            throw new TransactionException("Счет отправителя не активен");
        }
        if (receiver.getStatus() != com.petprojects.projectbanking.model.AccountStatus.ACTIVE) {
            throw new TransactionException("Счет получателя не активен");
        }
        BigDecimal amount = dto.getAmount();
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new TransactionException("Недостаточно средств");
        }
        if (!receiver.getUser().getFirstname().equalsIgnoreCase(dto.getToAccountName().trim())) {
            throw new TransactionException("Ошибка в имени получателя");
        }


        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction transaction = Transaction.builder()
                .senderAccount(sender)
                .receiverAccount(receiver)
                .amount(amount)
                .build();

        transactionRepository.save(transaction);

        return DtoPersonalTransact.builder()
                .senderAccountNumber(sender.getCountNumber())
                .receiverAccountNumber(receiver.getCountNumber())
                .amount(amount)
                .createdAt(transaction.getTransactionDate())
                .build();
    }
}