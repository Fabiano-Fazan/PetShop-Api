package com.petshop.api.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleResponseDto {

    private UUID id;
    private UUID clientId;
    private String clientName;
    private LocalDateTime saleDate;
    private BigDecimal totalValue;
    private String notes;
    private List<ProductSaleResponseDto> productSales;
}
