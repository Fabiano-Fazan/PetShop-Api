package com.petshop.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FinancialDtoResponse {

    private String description;
    private BigDecimal amount;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private Integer installment;
    private Boolean isPaid;
    private UUID clientName;
    private UUID saleId;

}
