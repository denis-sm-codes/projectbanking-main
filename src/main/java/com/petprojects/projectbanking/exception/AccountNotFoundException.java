package com.petprojects.projectbanking.exception;

public class AccountNotFoundException extends CustomException {
    public AccountNotFoundException(String accountNumber) {
        super("Аккаунт с номером " + accountNumber + " не найден");
    }
}