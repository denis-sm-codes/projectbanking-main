package com.petprojects.projectbanking.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class DtoTransaction {

//    @NotBlank(message = "Номер отправителя обязателен")
//    @Size(max = 20, message = "Номер счета отправителя слишком длинный")
//    private String fromAccountNumber;

//    @Size(max = 20, message = "Номер счета получателя слишком длинный")
    @NotBlank(message = "Номер получателя обязателен")
    private String toAccountNumber;

//    @Size(max = 50, message = "Имя получателя слишком длинное")
    @NotBlank(message = "Имя получательно обязательно")
    private String toAccountName;

    @NotNull(message = "Сумма обязательна")
    @DecimalMin(value = "0.01", message = "Сумма должна быть больше 0")
    private BigDecimal amount;
}