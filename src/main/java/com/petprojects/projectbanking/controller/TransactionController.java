package com.petprojects.projectbanking.controller;

import com.petprojects.projectbanking.dto.request.DtoTransaction;
import com.petprojects.projectbanking.dto.response.DtoPersonalTransact;
import com.petprojects.projectbanking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public DtoPersonalTransact transfer(@RequestBody DtoTransaction dto) {
        return transactionService.makeTransaction(dto);
    }
}