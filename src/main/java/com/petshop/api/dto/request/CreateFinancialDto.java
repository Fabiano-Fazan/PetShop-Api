package com.petshop.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CreateFinancialDto {

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "The price cannot be null")
    @Positive(message = "The price needs to be positive")
    private BigDecimal amount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate paymentDate;

    @NotNull(message = "The installment cannot be null")
    private Integer installment;

    private Boolean isPaid;

    @NotNull(message = "Client ID is required")
    private UUID clientId;

    @NotNull(message = "Sale ID is required")
    private UUID saleId;

}
