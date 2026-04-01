package com.petprojects.projectbanking.controller;

import com.petprojects.projectbanking.dto.request.DtoTransaction;
import com.petprojects.projectbanking.dto.response.DtoUserProfile;
import com.petprojects.projectbanking.dto.response.DtoPersonalTransact;

import com.petprojects.projectbanking.repository.UserRepository;
import com.petprojects.projectbanking.service.AuthService;
import com.petprojects.projectbanking.service.TransactionService;
import com.petprojects.projectbanking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TransactionService transactionService;
    private final AuthService authService;
    private final UserRepository userRepository;

    @GetMapping(value = "/profile", produces = "application/json")
    @PreAuthorize("hasRole('USER')")
    public DtoUserProfile getProfile() {
        return userService.getUserProfile();
    }

    @GetMapping("/transactions")
    @PreAuthorize("hasRole('USER')")
    public List<DtoPersonalTransact> getTransactions() {
        return userService.getUserTransactions();
    }

    @PostMapping("/transactions")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> newTransaction(@RequestBody @Valid DtoTransaction dto) {
        transactionService.makeTransaction(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok().build();
    }
}