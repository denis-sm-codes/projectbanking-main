package com.petprojects.projectbanking.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter

public class DtoPersonalTransact {

    private String senderAccountNumber;

    private String receiverAccountNumber;

    private BigDecimal amount;

    private LocalDateTime createdAt;
}