package com.petshop.api.domain;

import com.petshop.api.dto.request.CreateProductSaleDto;
import com.petshop.api.dto.request.CreateSaleDto;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.Product;
import com.petshop.api.model.entities.ProductSale;
import com.petshop.api.model.entities.Sale;
import com.petshop.api.repository.ProductRepository;
import com.petshop.api.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class SaleGenerator {
    private final ProductRepository productRepository;
    private final StockMovementService stockMovementService;

    public ProductSale generateProductSale(CreateProductSaleDto dto, Sale sale){
        Product product = productRepository.findWithLockById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + dto.getProductId()));

        return ProductSale.builder()
                .product(product)
                .quantity(dto.getQuantity())
                .unitPrice(dto.getPrice())
                .sale(sale)
                .build();
    }

    public BigDecimal calculateSaleTotal(CreateSaleDto dto){
        return dto.getProductSales().stream()
                .map(dto1 -> dto1.getPrice().multiply(BigDecimal.valueOf(dto1.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void registerStockMovementsFromSale(Sale sale){
        sale.getProductSales().forEach(item -> stockMovementService.registerOutput(
                item.getProduct(),
                item.getQuantity(),
                "SALE_ORDER_" + sale.getId(),
                item.getUnitPrice(),
                sale
        ));
    }

}
