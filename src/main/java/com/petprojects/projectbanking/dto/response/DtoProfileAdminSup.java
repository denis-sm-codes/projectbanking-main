package com.petprojects.projectbanking.dto.response;

import java.time.LocalDateTime;

public class DtoProfileAdminSup {

    private String firstname;
    private String secondname;

    private String userNumber;

    private String role;

    private String accountStatus;

    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "Имя: " + firstname + "\n" +
                "Фамилия: " + secondname + "\n" +
                "Номер счёта: " + userNumber + "\n" +
                "Статус: " + accountStatus + "\n" +
                "Роль: " + role + "\n" +
                "Создан - " + createdAt;
    }
}