package com.petprojects.projectbanking.controller;

import com.petprojects.projectbanking.dto.request.DtoCreateSupport;
import com.petprojects.projectbanking.dto.response.DtoListAccounts;
import com.petprojects.projectbanking.service.AccountService;
import com.petprojects.projectbanking.service.AdminService;
import com.petprojects.projectbanking.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AuthService authService;
    private final AccountService accountService;

    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createSupport(@RequestBody DtoCreateSupport dto) {
        adminService.createSupport(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/accounts")
    @PreAuthorize("hasRole('ADMIN')")
    public List<DtoListAccounts> getAllAccounts() {
        return adminService.getAllUsersForAdmin();
    }

    @PatchMapping("/users/{userNumber}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> disableUser(@PathVariable String userNumber) {
        accountService.disableUser(userNumber);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/users/{userNumber}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> enableUser(@PathVariable String userNumber) {
        accountService.enableUser(userNumber);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok().build();
    }
}