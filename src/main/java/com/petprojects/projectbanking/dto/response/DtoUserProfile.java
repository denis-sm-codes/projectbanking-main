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
}