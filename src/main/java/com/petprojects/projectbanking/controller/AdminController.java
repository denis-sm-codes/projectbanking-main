package com.petprojects.projectbanking.controller;

import com.petprojects.projectbanking.dto.request.DtoCreateSupport;
import com.petprojects.projectbanking.dto.response.DtoListAccounts;
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

    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ADMIN')")
    public void createSupport(@RequestBody DtoCreateSupport dto) {
        adminService.createSupport(dto);
    }

    @GetMapping("/accounts")
    @PreAuthorize("hasRole('ADMIN')")
    public List<DtoListAccounts> getAllAccounts() {
        return adminService.getAllUsersForAdmin();
    }

    @DeleteMapping("/delete-user/{userNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public void disableUser(@PathVariable String userNumber) {
        adminService.disableUser(userNumber);
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok().build();
    }
}