package com.petprojects.projectbanking.service;

import com.petprojects.projectbanking.exception.AccountNotFoundException;
import com.petprojects.projectbanking.model.Account;
import com.petprojects.projectbanking.model.AccountStatus;
import com.petprojects.projectbanking.model.User;
import com.petprojects.projectbanking.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account createAccount(User user) {
        Account account = Account.builder()
                .user(user)
                .countNumber(generateAccountNumber())
                .build();
        return accountRepository.save(account);
    }

    public Account getByAccountNumber(String accountNumber) {
        return accountRepository.findByCountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }


    public Account getByUserNumber(String userNumber) {
        return accountRepository.findByUser_UserNumber(userNumber)
                .orElseThrow(() -> new AccountNotFoundException(userNumber));
    }

    public void changeStatus(String accountNumber, AccountStatus status) {
        Account account = getByAccountNumber(accountNumber);
        account.setStatus(status);
        accountRepository.save(account);
    }

    private String generateAccountNumber() {
        final int cap1 = 2;
        final int cap2 = 5;
        final StringBuilder part1 = new StringBuilder("LV");
        String part2 = "1234567890";
        final StringBuilder part3 = new StringBuilder("HABA");

        Random random1 = new Random();
        StringBuilder stringBuilder1 = new StringBuilder(cap1);
        StringBuilder stringBuilder2 = new StringBuilder(cap2);

        for (int j = 0; j < cap1; j++) {
            stringBuilder1.append(part2.charAt(random1.nextInt(part2.length())));
        }
        for (int j = 0; j < cap2; j++) {
            stringBuilder2.append(part2.charAt(random1.nextInt(part2.length())));
        }
        return part1.append(stringBuilder1).append(part3).append(stringBuilder2).toString();
    }
}