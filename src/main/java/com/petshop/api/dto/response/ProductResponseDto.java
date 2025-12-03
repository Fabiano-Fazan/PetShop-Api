package com.petshop.api.dto.response;


import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Integer quantityInStock;
}
