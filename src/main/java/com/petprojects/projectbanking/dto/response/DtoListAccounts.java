package com.petprojects.projectbanking.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class DtoListAccounts {

    private String firstname;
    private String secondname;

    private String userNumber;    // 7-значный
    private String countNumber;   // LVxxHABAxxxxxx (для аккаунтов ROLE_USER)

    private BigDecimal balance;   // null для SUPORT/ADMIN

    private boolean active;       // enabled

    private LocalDateTime createdAt;

    private String role;          // ADMIN / SUPPORT / USER

    private static String activity(boolean check){
        return check ? "Активен" : "Не Активен";
    }

    @Override
    public String toString() {
        return "Пользователь: " + firstname + " " + secondname + "  " + userNumber
                + "  Статус: " + activity(active) + "   время создания: " + createdAt;
    }
}