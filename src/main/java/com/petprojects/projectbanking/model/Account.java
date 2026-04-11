package com.petprojects.projectbanking.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.petprojects.projectbanking.security.date.DateCondit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AccountStatus status = AccountStatus.ACTIVE;

    @Column(name = "count_number", nullable = false, unique = true, length = 15)
    private String countNumber;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    @JsonSerialize(using = DateCondit.class)
    private LocalDateTime createdAt;
}