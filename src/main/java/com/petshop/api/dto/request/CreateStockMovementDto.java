package com.petshop.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class CreateStockMovementDto {

    @NotNull(message = "The quantity cannot be null")
    @Positive(message = "The quantity needs to be positive")
    private Integer quantity;

    @NotBlank(message = "The description cannot be null")
    private String description;

    private String invoice;

    @NotNull(message = "The price cannot be null")
    @Positive(message = "The price needs to be positive")
    private BigDecimal price;
}
