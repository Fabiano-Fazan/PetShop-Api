package com.petshop.api.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "financial")
public class Financial {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(nullable = false)
    private Boolean isPaid;

    @Column(nullable = false)
    private Integer installment;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "monetary_type")
    private MonetaryType monetaryType;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @OneToMany(mappedBy = "financial", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FinancialPayment> financialPayments = new ArrayList<>();

    @PrePersist
    public void createdFinancialAt() {
        if (dateCreated == null) dateCreated = LocalDate.now();
        if (balance == null) balance = amount;
        }
}
