package com.petshop.api.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "financial_payments")
public class FinancialPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private BigDecimal paidAmount;

    @Column(nullable = false)
    private LocalDate paymentDate;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "monetary_type")
    private MonetaryType monetaryType;

    @ManyToOne
    @JoinColumn(name = "financial_id", nullable = false)
    private Financial financial;
}
