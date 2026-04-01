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

    private String userNumber;

    private String countNumber;

    private BigDecimal balance;

    private boolean active;

    private LocalDateTime createdAt;

    private String role;
}