package com.petprojects.projectbanking.exception;

public class AccessDeniedException extends CustomException{

    public AccessDeniedException(String message) {
        super("Доступ запрещен: счет " + message + " вам не принадлежит");
    }
}