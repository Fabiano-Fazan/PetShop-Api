package com.petshop.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CreateFinancialPaymentDto {

    @NotNull(message = "The price cannot be null")
    @Positive(message = "The price needs to be positive")
    private BigDecimal paidAmount;

    @NotNull(message = "The payment date cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate paymentDate;

    private UUID monetaryTypeId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextDueDate;

    private String notes;
}


