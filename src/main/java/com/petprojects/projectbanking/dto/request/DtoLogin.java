package com.petprojects.projectbanking.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DtoLogin {

    @NotBlank
    private String userNumber;
}