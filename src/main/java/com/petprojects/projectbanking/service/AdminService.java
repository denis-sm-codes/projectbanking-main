package com.petprojects.projectbanking.service;

import com.petprojects.projectbanking.dto.request.DtoCreateSupport;
import com.petprojects.projectbanking.dto.response.DtoCreatedPerson;
import com.petprojects.projectbanking.dto.response.DtoListAccounts;
import com.petprojects.projectbanking.exception.UserNotFoundException;
import com.petprojects.projectbanking.model.*;
import com.petprojects.projectbanking.repository.AccountRepository;
import com.petprojects.projectbanking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public DtoCreatedPerson createSupport(DtoCreateSupport dto) {
        User user = User.builder()
                .firstname(dto.getFirstName().trim())
                .secondname(dto.getSecondName().trim())
                .email(dto.getEmail().trim())
                .role(Role.SUPPORT)
                .enabled(true)
                .build();
        userRepository.save(user);

        DtoCreatedPerson dtoCreatedPerson = new DtoCreatedPerson();
        dtoCreatedPerson.setFirstName(dto.getFirstName());
        dtoCreatedPerson.setSecondName(dto.getSecondName());
        dtoCreatedPerson.setEmail(dto.getEmail());
        dtoCreatedPerson.setRole(user.getRole());
        dtoCreatedPerson.setUserNumber(user.getUserNumber());

        return dtoCreatedPerson;
    }

    public List<DtoListAccounts> getAllUsersForAdmin() {
        return userRepository.findAll().stream().filter(user ->
            user.getRole() == Role.ADMIN || user.getRole() == Role.SUPPORT || user.getRole() == Role.USER)
                .map(user -> {
                    DtoListAccounts dto = new DtoListAccounts();
                    dto.setFirstname(user.getFirstname());
                    dto.setSecondname(user.getSecondname());
                    dto.setUserNumber(user.getUserNumber());
                    dto.setRole(user.getRole().name());
                    dto.setActive(user.isEnabled());
                    dto.setCreatedAt(user.getCreatedAt());

                    Account account = user.getAccount();
                    dto.setCountNumber((account != null && user.getRole() == Role.USER) ? account.getCountNumber() : null);
                    dto.setBalance((account != null && user.getRole() == Role.USER) ? account.getBalance() : null);
            return dto;
        }).toList();
    }

    public void deleteSupport(String number){
        User user = userRepository.findByUserNumber(number)
                .orElseThrow(() -> new UserNotFoundException(number));
        userRepository.delete(user);
    }
}