package com.petshop.api.service;

import com.petshop.api.dto.request.CreateStockMovementDto;
import com.petshop.api.exception.InsufficientStockException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.Product;
import com.petshop.api.model.entities.Sale;
import com.petshop.api.model.entities.StockMovement;
import com.petshop.api.repository.ProductRepository;
import com.petshop.api.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class StockMovementService {
    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void registerInput(CreateStockMovementDto createStockMovementDTO){
        Product product = productRepository.findWithLockById(createStockMovementDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + createStockMovementDTO.getProductId()));
        this.registerInput(product, createStockMovementDTO.getQuantity(), createStockMovementDTO.getDescription(), createStockMovementDTO.getInvoice(), createStockMovementDTO.getPrice());
    }

    @Transactional
    public void registerInput(Product product, Integer quantity, String description, String invoice, BigDecimal price){
        Product productDb = productRepository.findById(product.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + product.getId()));
        productDb.setQuantityInStock(product.getQuantityInStock() + quantity);
        productRepository.save(productDb);

        StockMovement stockMovement = StockMovement.newInput(productDb, quantity, description, invoice, price);
        stockMovementRepository.save(stockMovement);
    }

    @Transactional
    public void registerOutput(CreateStockMovementDto createStockMovementDTO){
        Product product = productRepository.findWithLockById(createStockMovementDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + createStockMovementDTO.getProductId()));
        this.registerOutput(product, createStockMovementDTO.getQuantity(), createStockMovementDTO.getDescription(),createStockMovementDTO.getPrice(), null);
    }

    @Transactional
    public void registerOutput(Product product, Integer quantity, String description, BigDecimal price, Sale sale){
        Product productDb = productRepository.findById(product.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + product.getId()));
        if(productDb.getQuantityInStock() < quantity){
            throw new InsufficientStockException("Not enough stock for product " + productDb.getName() + "Requested: " + quantity + " Available: " + productDb.getQuantityInStock());
        }
        productDb.setQuantityInStock(product.getQuantityInStock() - quantity);
        productRepository.save(productDb);

        StockMovement stockMovement = StockMovement.newOutput(productDb, quantity,description,sale, price);
        stockMovementRepository.save(stockMovement);
    }
}