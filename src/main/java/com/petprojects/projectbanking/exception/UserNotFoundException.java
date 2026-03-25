package com.petprojects.projectbanking.exception;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(String userNumber) {
        super("Пользователь с номером " + userNumber + " не найден");
    }
}