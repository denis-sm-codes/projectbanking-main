package com.petprojects.projectbanking.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.petprojects.projectbanking.security.date.DateCondit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_number", unique = true, nullable = false, length = 7)
    private String userNumber;

    @PrePersist
    private void generateUserNumber() {
        if (this.userNumber == null) {
            this.userNumber = generate();
        }
    }

    private static String generate() {
        int min = 0_000_000;
        int max = 9_999_999;
        int number = (int) (Math.random() * (max - min + 1) + min);
        DecimalFormat df = new DecimalFormat("0000000");
        return df.format(number);
    }

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false)
    private String firstname;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false)
    private String secondname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @NotBlank(message = "Email обязателен")
    @Column(nullable = false, unique = true, length = 100)
    @Email(message = "Некорректный email")
    @Size(max = 100, message = "Email не должен превышать 100 символов")
    private String email;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    @JsonSerialize(using = DateCondit.class)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();
}