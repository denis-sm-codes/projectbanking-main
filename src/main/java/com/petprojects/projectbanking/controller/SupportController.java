package com.petprojects.projectbanking.controller;

import com.petprojects.projectbanking.dto.request.DtoCreateSupport;
import com.petprojects.projectbanking.dto.request.DtoCreateUser;
import com.petprojects.projectbanking.dto.response.DtoListAccounts;
import com.petprojects.projectbanking.dto.response.DtoListTransact;
import com.petprojects.projectbanking.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/support")
@RequiredArgsConstructor
public class SupportController {

    private final SupportService supportService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('SUPPORT')")
    @SuppressWarnings("unused")
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

    @DeleteMapping("/delete-user/{userNumber}")
    @PreAuthorize("hasRole('SUPPORT')")
    public void disableUser(@PathVariable String userNumber) {
        supportService.disableUser(userNumber);
    }
}