package com.petprojects.projectbanking.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.petprojects.projectbanking.security.date.DateCondit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private Account senderAccount;

    @ManyToOne
    @JoinColumn(name = "receiver_account_id", nullable = false)
    private Account receiverAccount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    @JsonSerialize(using = DateCondit.class)
    private LocalDateTime transactionDate;

    @Column(length = 20)
    private String description;
}