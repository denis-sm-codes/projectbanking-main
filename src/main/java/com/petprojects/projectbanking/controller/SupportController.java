package com.petprojects.projectbanking.controller;

import com.petprojects.projectbanking.dto.request.DtoCreateUser;
import com.petprojects.projectbanking.dto.response.DtoListAccounts;
import com.petprojects.projectbanking.dto.response.DtoListTransact;
import com.petprojects.projectbanking.service.AccountService;
import com.petprojects.projectbanking.service.AuthService;
import com.petprojects.projectbanking.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/support")
@RequiredArgsConstructor
public class SupportController {

    private final SupportService supportService;
    private final AuthService authService;
    private final AccountService accountService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('SUPPORT')")
    public void createUser(@RequestBody DtoCreateUser dto) {
        supportService.createUser(dto);
    }

    @GetMapping("/accounts")
    @PreAuthorize("hasRole('SUPPORT')")
    public List<DtoListAccounts> getAllAccounts() {
        return supportService.getAllUsersForSupport();
    }

    @GetMapping("/transactions/{accountNumber}")
    @PreAuthorize("hasRole('SUPPORT')")
    public List<DtoListTransact> getTransactions(@PathVariable String accountNumber) {
        return supportService.getTransactionsByAccountNumber(accountNumber);
    }

    @PatchMapping("/users/{userNumber}/disable")
    @PreAuthorize("hasRole('SUPPORT')")
    public ResponseEntity<Void> disableUser(@PathVariable String userNumber) {
    accountService.disableUser(userNumber);
    return ResponseEntity.ok().build();
    }

    @PatchMapping("/users/{userNumber}/enable")
    @PreAuthorize("hasRole('SUPPORT')")
    public ResponseEntity<Void> enableUser(@PathVariable String userNumber) {
        accountService.enableUser(userNumber);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('SUPPORT')")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok().build();
    }
}