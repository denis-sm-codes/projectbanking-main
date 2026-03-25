package com.petprojects.projectbanking.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class DtoUserProfile {

    private String firstname;
    private String secondname;

    private BigDecimal balance;

    private String userNumber;

    private String role;

    private String accountStatus;

    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return "Имя: " + firstname + "\n" +
                "Фамилия: " + secondname + "\n" +
                "Номер счёта: " + userNumber + "\n" +
                "Счёт - " + balance + "\n" +
                "Статус: " + accountStatus + "\n" +
                "Роль: " + role + "\n" +
                "Создан - " + createdAt;
    }
}