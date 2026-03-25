package com.petprojects.projectbanking.service;

import com.petprojects.projectbanking.dto.response.DtoUserProfile;
import com.petprojects.projectbanking.dto.response.DtoPersonalTransact;
import com.petprojects.projectbanking.exception.UserNotFoundException;
import com.petprojects.projectbanking.model.Account;
import com.petprojects.projectbanking.model.Transaction;
import com.petprojects.projectbanking.model.User;
import com.petprojects.projectbanking.repository.AccountRepository;
import com.petprojects.projectbanking.repository.TransactionRepository;
import com.petprojects.projectbanking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public DtoUserProfile getUserProfile() {

        String userNumber = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByUserNumber(userNumber)
                .orElseThrow(() -> new UserNotFoundException(userNumber));

        Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Аккаунт не найден"));

        DtoUserProfile dto = new DtoUserProfile();
        dto.setFirstname(user.getFirstname());
        dto.setSecondname(user.getSecondname());
        dto.setUserNumber(user.getUserNumber());
        dto.setRole(user.getRole().name());
        dto.setBalance(account.getBalance());
        dto.setAccountStatus(account.getStatus().name());
        dto.setCreatedAt(user.getCreatedAt());

        return dto;
    }

    public List<DtoPersonalTransact> getUserTransactions() {

        String userNumber = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Account account = accountRepository.findByUser_UserNumber(userNumber)
                .orElseThrow(() -> new RuntimeException("Аккаунт пользователя не найден"));

        List<Transaction> transactions =
                transactionRepository.findBySenderAccountOrReceiverAccount(account, account);

        return transactions.stream()
                .map(this::mapTransactionToDto)
                .toList();
    }

    // --- Вспомогательный метод для маппинга транзакции ---
    private DtoPersonalTransact mapTransactionToDto(Transaction transaction) {
        DtoPersonalTransact dto = new DtoPersonalTransact();
        dto.setSenderAccountNumber(transaction.getSenderAccount() != null
                ? transaction.getSenderAccount().getCountNumber() : "SYSTEM");
        dto.setReceiverAccountNumber(transaction.getReceiverAccount().getCountNumber());
        dto.setAmount(transaction.getAmount());
        dto.setCreatedAt(transaction.getTransactionDate());
        return dto;
    }
}