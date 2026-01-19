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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockMovementServiceTest {

    @InjectMocks
    private StockMovementService stockMovementService;

    @Mock
    private StockMovementRepository stockMovementRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ValidatorEntities validatorEntities;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Captor
    private ArgumentCaptor<StockMovement> stockMovementCaptor;


    @Test
    @DisplayName("Should register input successfully via Entity method")
    void registerInput_EntityMethod_ShouldIncreaseStockAndSaveMovement() {

        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setQuantityInStock(10);
        product.setName("Shampoo");

        Integer inputQuantity = 5;
        String description = "New Box";
        BigDecimal price = new BigDecimal("15.50");

        when(validatorEntities.validate(productId, productRepository, "Product")).thenReturn(product);

        stockMovementService.registerInput(product, inputQuantity, description, "INV-001", price, null);

        verify(productRepository).save(productCaptor.capture());
        Product savedProduct = productCaptor.getValue();
        assertThat(savedProduct.getQuantityInStock()).isEqualTo(15);
        verify(stockMovementRepository).save(stockMovementCaptor.capture());
        StockMovement savedMovement = stockMovementCaptor.getValue();
        assertThat(savedMovement.getProduct()).isEqualTo(product);
        assertThat(savedMovement.getQuantity()).isEqualTo(inputQuantity);
        assertThat(savedMovement.getType()).isEqualTo(TypeMovement.INPUT);
        assertThat(savedMovement.getInvoice()).isEqualTo("INV-001");
    }


    @Test
    @DisplayName("Should register input successfully via DTO method")
    void registerInput_DtoMethod_ShouldFindProductAndCallInternalLogic() {

        UUID id = UUID.randomUUID();
        CreateStockMovementDto dto = new CreateStockMovementDto();
        dto.setQuantity(10);
        dto.setDescription("Restock");
        dto.setInvoice("INV-999");
        dto.setPrice(BigDecimal.TEN);

        Product product = new Product();
        product.setId(id);
        product.setQuantityInStock(0);

        when(productRepository.findWithLockById(id)).thenReturn(Optional.of(product));
        when(validatorEntities.validate(id, productRepository, "Product")).thenReturn(product);

        stockMovementService.registerInput(id, dto);

        verify(productRepository).save(product);
        verify(stockMovementRepository).save(any(StockMovement.class));
    }


    @Test
    @DisplayName("Should throw ResourceNotFoundException when registering input for non-existent product")
    void registerInput_DtoMethod_ShouldThrowException_WhenProductNotFound() {

        UUID id = UUID.randomUUID();
        CreateStockMovementDto dto = new CreateStockMovementDto();

        when(productRepository.findWithLockById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> stockMovementService.registerInput(id, dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product not found");

        verify(validatorEntities, never()).validate(id, productRepository, "Product");
        verify(productRepository, never()).save(any());
    }


    @Test
    @DisplayName("Should register output successfully via Entity method when stock is sufficient")
    void registerOutput_EntityMethod_ShouldDecreaseStockAndSaveMovement() {

        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setQuantityInStock(20);
        product.setName("Dog Food");

        Integer outputQuantity = 5;
        String description = "Sale #123";
        BigDecimal price = new BigDecimal("50.00");
        Sale sale = new Sale();

        when(validatorEntities.validate(productId, productRepository, "Product")).thenReturn(product);

        stockMovementService.registerOutput(product, outputQuantity, description, price, sale);

        verify(productRepository).save(productCaptor.capture());
        Product savedProduct = productCaptor.getValue();
        assertThat(savedProduct.getQuantityInStock()).isEqualTo(15);
        verify(stockMovementRepository).save(stockMovementCaptor.capture());
        StockMovement savedMovement = stockMovementCaptor.getValue();
        assertThat(savedMovement.getType()).isEqualTo(TypeMovement.OUTPUT);
        assertThat(savedMovement.getSale()).isEqualTo(sale);
    }

    @Test
    @DisplayName("Should throw InsufficientStockException when stock is not enough")
    void registerOutput_EntityMethod_ShouldThrowException_WhenInsufficientStock() {

        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setName("Rare Item");
        product.setQuantityInStock(2);

        Integer requestedQuantity = 5;

        when(validatorEntities.validate(productId, productRepository, "Product")).thenReturn(product);

        assertThatThrownBy(() -> stockMovementService.registerOutput(product, requestedQuantity, "Desc", BigDecimal.TEN, null))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Not enough stock for product Rare Item. Requested: 5, Available: 2");

        verify(productRepository, never()).save(any());
        verify(stockMovementRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should register output successfully via DTO method")
    void registerOutput_DtoMethod_ShouldFindProductAndCallInternalLogic() {

        UUID id = UUID.randomUUID();
        CreateStockMovementDto dto = new CreateStockMovementDto();
        dto.setQuantity(1);
        dto.setPrice(BigDecimal.TEN);
        dto.setDescription("Loss");
        Product product = new Product();
        product.setId(id);
        product.setQuantityInStock(10);

        when(productRepository.findWithLockById(id)).thenReturn(Optional.of(product));
        when(validatorEntities.validate(id, productRepository, "Product")).thenReturn(product);

        stockMovementService.registerOutput(id, dto);

        verify(productRepository).save(product);
        verify(stockMovementRepository).save(any(StockMovement.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when registering output for non-existent product")
    void registerOutput_DtoMethod_ShouldThrowException_WhenProductNotFound() {

        UUID id = UUID.randomUUID();
        CreateStockMovementDto dto = new CreateStockMovementDto();

        when(productRepository.findWithLockById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> stockMovementService.registerOutput(id, dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product not found");

        verify(stockMovementRepository, never()).save(any());
    }
}