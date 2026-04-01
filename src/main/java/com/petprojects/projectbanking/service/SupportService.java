package com.petprojects.projectbanking.service;

import com.petprojects.projectbanking.dto.request.DtoCreateUser;
import com.petprojects.projectbanking.dto.response.DtoCreatedPerson;
import com.petprojects.projectbanking.dto.response.DtoListAccounts;
import com.petprojects.projectbanking.dto.response.DtoListTransact;
import com.petprojects.projectbanking.exception.AccountNotFoundException;
import com.petprojects.projectbanking.model.Account;
import com.petprojects.projectbanking.model.Role;
import com.petprojects.projectbanking.model.Transaction;
import com.petprojects.projectbanking.model.User;
import com.petprojects.projectbanking.repository.AccountRepository;
import com.petprojects.projectbanking.repository.TransactionRepository;
import com.petprojects.projectbanking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupportService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AccountService accountService;

    public DtoCreatedPerson createUser(DtoCreateUser dto) {
        User user = User.builder()
                .firstname(dto.getFirstname().trim())
                .secondname(dto.getSecondname().trim())
                .email(dto.getEmail().trim())
                .role(Role.USER)
                .enabled(true)
                .build();
        userRepository.save(user);

        Account account = accountService.createAccount(user);

        DtoCreatedPerson dtoCreatedPerson = new DtoCreatedPerson();
        dtoCreatedPerson.setFirstName(user.getFirstname());
        dtoCreatedPerson.setSecondName(user.getSecondname());
        dtoCreatedPerson.setUserNumber(user.getUserNumber());
        dtoCreatedPerson.setCountNumber(account.getCountNumber());

        return dtoCreatedPerson;
    }

    public List<DtoListAccounts> getAllUsersForSupport() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.USER)  // только пользователи USER
                .map(user -> {
                    DtoListAccounts dto = new DtoListAccounts();
                    dto.setFirstname(user.getFirstname());
                    dto.setSecondname(user.getSecondname());
                    dto.setUserNumber(user.getUserNumber());
                    dto.setRole(user.getRole().name());
                    dto.setActive(user.isEnabled());
                    dto.setCreatedAt(user.getCreatedAt());

                    Account account = user.getAccount();
                    if (account != null) {
                        dto.setCountNumber(account.getCountNumber());
                        dto.setBalance(account.getBalance());
                    } else {
                        dto.setCountNumber(null);
                        dto.setBalance(null);
                    }
                    return dto;
                })
                .toList();
    }

    public List<DtoListTransact> getTransactionsByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByCountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));

        List<Transaction> transactions = transactionRepository.findBySenderAccountOrReceiverAccount(account, account);

        return transactions.stream().map(this::mapTransactionToDto).collect(Collectors.toList());
    }

    // --- Вспомогательные методы для маппинга ---
    private DtoListTransact mapTransactionToDto(Transaction transaction) {
        DtoListTransact dto = new DtoListTransact();
        dto.setSenderAccountNumber(transaction.getSenderAccount() != null
                ? transaction.getSenderAccount().getCountNumber() : "SYSTEM");
        dto.setReceiverAccountNumber(transaction.getReceiverAccount().getCountNumber());
        dto.setAmount(transaction.getAmount());
        dto.setCreatedAt(transaction.getTransactionDate());
        return dto;
    }
}