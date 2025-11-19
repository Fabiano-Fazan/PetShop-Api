package com.petshop.api.model.entities;

import com.petshop.api.dto.response.FinancialDtoResponse;
import jakarta.persistence.*;
import lombok.*;

import java.lang.ScopedValue;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    private LocalDate dateCreated;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private LocalDate paymentDate;

    @Column(nullable = false)
    private Boolean isPaid;

    @Column(nullable = false)
    private Integer installment;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;


    @PrePersist
    public void createdFinancialAt() {
        if (dateCreated == null) {
            dateCreated = LocalDate.now();
        }
    }

    public static Financial createFinancial(Client client, Sale sale, String description, BigDecimal amount, LocalDate dueDate, LocalDate paymentDate,Boolean isPaid, Integer installment){
        return Financial.builder()
                .client(client)
                .sale(sale)
                .description(description)
                .amount(amount)
                .dueDate(dueDate)
                .paymentDate(paymentDate)
                .isPaid(false)
                .installment(installment)
                .build();
    }

}
