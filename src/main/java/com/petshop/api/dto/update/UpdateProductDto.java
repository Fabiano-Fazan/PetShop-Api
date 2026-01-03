package com.petshop.api.dto.update;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class UpdateProductDto {

    @Size(min = 3, max = 100, message = "The name must be between 3 and 100 characters")
    private String name;

    @Size(min = 10, max = 255, message = "The description must be between 10 and 255 characters long")
    private String description;

    @Positive(message = "The price needs to be positive")
    private BigDecimal price;

    private UUID categoryId;

}
