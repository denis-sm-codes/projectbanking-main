package com.petprojects.projectbanking.controller;

import com.petprojects.projectbanking.dto.request.DtoLogin;
import com.petprojects.projectbanking.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public String login(@RequestBody DtoLogin dto) {
        return authService.login(dto);
    }
}