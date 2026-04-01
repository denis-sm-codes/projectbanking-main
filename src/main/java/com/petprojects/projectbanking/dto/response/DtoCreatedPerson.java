package com.petprojects.projectbanking.dto.response;

import com.petprojects.projectbanking.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoCreatedPerson {

    private String firstName;

    private String secondName;

    private String countNumber;

    private String userNumber;

    private Enum<Role> role;
}