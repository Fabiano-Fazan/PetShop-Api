package com.petshop.api.service;

import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateStockMovementDto;
import com.petshop.api.exception.InsufficientStockException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.Product;
import com.petshop.api.model.entities.Sale;
import com.petshop.api.model.entities.StockMovement;
import com.petshop.api.model.enums.TypeMovement;
import com.petshop.api.repository.ProductRepository;
import com.petshop.api.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;
    private final ValidatorEntities validatorEntities;

    @Transactional
    public void registerInput(Product product, Integer quantity, String description, String invoice, BigDecimal price, Sale sale){
        var productSaved = validatorEntities.validate(product.getId(), productRepository, "Product");
        productSaved.setQuantityInStock(product.getQuantityInStock() + quantity);
        productRepository.save(productSaved);

        StockMovement stockMovement = StockMovement.builder()
                .product(productSaved)
                .quantity(quantity)
                .description(description)
                .invoice(invoice)
                .price(price)
                .sale(sale)
                .type(TypeMovement.INPUT)
                .build();
        stockMovementRepository.save(stockMovement);
    }

    @Transactional
    public void registerInput(UUID id, CreateStockMovementDto dto){
        var product = productRepository.findWithLockById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        this.registerInput(product, dto.getQuantity(), dto.getDescription(), dto.getInvoice(), dto.getPrice(), null);
    }

    @Transactional
    public void registerOutput(Product product, Integer quantity, String description, BigDecimal price, Sale sale){
        var productSaved = validatorEntities.validate(product.getId(), productRepository, "Product");
        if(productSaved.getQuantityInStock() < quantity){
            throw new InsufficientStockException("Not enough stock for product %s. Requested: %s, Available: %s"
                    .formatted(productSaved.getName(), quantity, productSaved.getQuantityInStock()));
        }
        productSaved.setQuantityInStock(product.getQuantityInStock() - quantity);
        productRepository.save(productSaved);

        StockMovement stockMovement = StockMovement.builder()
                .product(productSaved)
                .quantity(quantity)
                .description(description)
                .price(price)
                .sale(sale)
                .type(TypeMovement.OUTPUT)
                .build();
        stockMovementRepository.save(stockMovement);
    }

    @Transactional
    public void registerOutput( UUID id, CreateStockMovementDto dto){
        Product product = productRepository.findWithLockById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        this.registerOutput(product, dto.getQuantity(), dto.getDescription(), dto.getPrice(), null);
    }
}