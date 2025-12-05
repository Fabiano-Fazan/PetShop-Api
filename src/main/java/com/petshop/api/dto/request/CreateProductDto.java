package com.petshop.api.dto.request;


import com.petshop.api.model.entities.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class CreateProductDto {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "The name must be between 3 and 100 characters")
    private String name;

    @Size(min = 10, max = 255, message = "The description must be between 10 and 255 characters")
    private String description;

    @NotNull(message = "The price cannot be null")
    @Positive(message = "The price needs to be positive")
    private BigDecimal price;

    private UUID categoryId;

    private Integer quantityInStock;
}
