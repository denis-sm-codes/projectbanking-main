package com.petprojects.projectbanking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DtoAuthResponse {
    private String accessToken;
    private String refreshToken;
}