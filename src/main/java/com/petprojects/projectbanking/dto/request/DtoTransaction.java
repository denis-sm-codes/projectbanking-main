package com.petprojects.projectbanking.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class DtoTransaction {

    @NotBlank(message = "Номер получателя обязателен")
    private String toAccountNumber;

    @NotBlank(message = "Имя получательно обязательно")
    private String toAccountName;

    @NotNull(message = "Сумма обязательна")
    @DecimalMin(value = "0.01", message = "Сумма должна быть больше 0")
    private BigDecimal amount;
}