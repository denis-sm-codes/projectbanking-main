package com.petprojects.projectbanking.dto.response;

import com.petprojects.projectbanking.model.Role;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DtoCreatedPerson {

    private String firstName;

    private String secondName;

    private String countNumber;

    private String userNumber;

    private String email;

    private Enum<Role> role;
}