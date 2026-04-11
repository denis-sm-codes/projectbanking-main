package com.petprojects.projectbanking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DtoAuthResponse {

    private String accessToken;

    private String refreshToken;
}