package com.firm.brokage.service.demo.entities;

import com.firm.brokage.service.demo.enumaration.TransactionType;
import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column
    private BigDecimal amount;

    @Column
    private String iban;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
}
