package com.petshop.api.dto.request;

import com.petshop.api.model.enums.SalePaymentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateSaleDto {

    @NotNull(message = "Client ID is required")
    private UUID clientId;

    @NotEmpty(message = "Product sales list is required")
    @Valid
    private List<CreateProductSaleDto> productSales;

    @Min(value = 1, message = "The sale must have at least one installment.")
    private Integer installments;

    @Min(value = 0, message = "The interval must be positive")
    private Integer intervalDays;

    @NotNull(message = "Payment type is required")
    private SalePaymentType paymentType;

    private String notes;

}
