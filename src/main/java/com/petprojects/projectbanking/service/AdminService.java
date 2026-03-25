package com.petprojects.projectbanking.service;

import com.petprojects.projectbanking.dto.request.DtoCreateSupport;
import com.petprojects.projectbanking.dto.response.DtoListAccounts;
import com.petprojects.projectbanking.model.*;
import com.petprojects.projectbanking.repository.AccountRepository;
import com.petprojects.projectbanking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public User createSupport(DtoCreateSupport dto) {
        User user = User.builder()
                .firstname(dto.getFirstname().trim())
                .secondname(dto.getSecondname().trim())
                .email(dto.getEmail().trim())
                .role(Role.SUPPORT)
                .enabled(true)
                .build();
        userRepository.save(user);
        return user;
    }

    public List<DtoListAccounts> getAllUsersForAdmin() {
        return userRepository.findAll().stream().map(user -> {
            DtoListAccounts dto = new DtoListAccounts();
            dto.setFirstname(user.getFirstname());
            dto.setSecondname(user.getSecondname());
            dto.setUserNumber(user.getUserNumber());
            dto.setRole(user.getRole().name());
            dto.setActive(user.isEnabled());
            dto.setCreatedAt(user.getCreatedAt());

            Account account = user.getAccount();
            if (account != null && user.getRole() == Role.USER) {
                dto.setCountNumber(account.getCountNumber());
                dto.setBalance(account.getBalance());
            } else {
                dto.setCountNumber(null);
                dto.setBalance(null);
            }

            return dto;
        }).toList();
    }

    public void changeAccountStatus(String accountNumber, AccountStatus status) {
        accountService.changeStatus(accountNumber, status);
    }

    public void disableUser(String userNumber) {
        User user = userRepository.findByUserNumber(userNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(false);
        userRepository.save(user);
    }
}