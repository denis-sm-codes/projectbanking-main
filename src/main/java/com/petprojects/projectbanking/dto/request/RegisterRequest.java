package com.petprojects.projectbanking.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {

    private String firstname;

    private String secondname;

    private String email;

    private String password;
}