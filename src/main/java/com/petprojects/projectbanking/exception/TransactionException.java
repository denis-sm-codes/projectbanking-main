package com.petprojects.projectbanking.exception;

public class TransactionException extends CustomException {
    public TransactionException(String message) {
        super("Ошибка транзакции: " + message);
    }
}