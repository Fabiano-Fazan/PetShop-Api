package com.petshop.api.domain.sale;

import com.petshop.api.dto.request.CreateProductSaleDto;
import com.petshop.api.dto.request.CreateSaleDto;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.Product;
import com.petshop.api.model.entities.ProductSale;
import com.petshop.api.model.entities.Sale;
import com.petshop.api.repository.ProductRepository;
import com.petshop.api.service.StockMovementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleGeneratorTest {

    @InjectMocks
    private SaleGenerator saleGenerator;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockMovementService stockMovementService;


    @Test
    @DisplayName("Should generate ProductSale entity when product exists")
    void generateProductSale_ShouldReturnEntity_WhenProductExists() {

        UUID productId = UUID.randomUUID();
        CreateProductSaleDto dto = new CreateProductSaleDto();
        dto.setProductId(productId);
        dto.setQuantity(5);
        dto.setPrice(new BigDecimal("10.00"));
        Sale sale = new Sale();
        Product product = new Product();
        product.setId(productId);

        when(productRepository.findWithLockById(productId)).thenReturn(Optional.of(product));

        ProductSale result = saleGenerator.generateProductSale(dto, sale);

        assertThat(result).isNotNull();
        assertThat(result.getProduct()).isEqualTo(product);
        assertThat(result.getSale()).isEqualTo(sale);
        assertThat(result.getQuantity()).isEqualTo(5);
        assertThat(result.getUnitPrice()).isEqualTo(new BigDecimal("10.00"));

        verify(productRepository).findWithLockById(productId);
    }


    @Test
    @DisplayName("Should throw ResourceNotFoundException when product does not exist")
    void generateProductSale_ShouldThrowException_WhenProductNotFound() {

        UUID productId = UUID.randomUUID();
        CreateProductSaleDto dto = new CreateProductSaleDto();
        dto.setProductId(productId);
        Sale sale = new Sale();

        when(productRepository.findWithLockById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> saleGenerator.generateProductSale(dto, sale))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product not found");
    }


    @Test
    @DisplayName("Should calculate total value correctly summing all items")
    void calculateSaleTotal_ShouldReturnSumOfItems() {

        CreateProductSaleDto item1 = new CreateProductSaleDto();
        item1.setQuantity(2);
        item1.setPrice(new BigDecimal("10.00"));
        CreateProductSaleDto item2 = new CreateProductSaleDto();
        item2.setQuantity(3);
        item2.setPrice(new BigDecimal("5.00"));
        CreateSaleDto saleDto = new CreateSaleDto();
        saleDto.setProductSales(List.of(item1, item2));

        BigDecimal result = saleGenerator.calculateSaleTotal(saleDto);

        assertThat(result).isEqualByComparingTo(new BigDecimal("35.00"));
    }


    @Test
    @DisplayName("Should return zero when list is empty")
    void calculateSaleTotal_ShouldReturnZero_WhenListEmpty() {

        CreateSaleDto saleDto = new CreateSaleDto();
        saleDto.setProductSales(List.of());

        BigDecimal result = saleGenerator.calculateSaleTotal(saleDto);

        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
    }


    @Test
    @DisplayName("Should register output stock movement for each item in sale")
    void registerStockMovementsFromSale_ShouldCallServiceForEachItem() {

        UUID saleId = UUID.randomUUID();
        Sale sale = new Sale();
        sale.setId(saleId);

        Product product1 = new Product();
        ProductSale item1 = ProductSale.builder()
                .product(product1)
                .quantity(2)
                .unitPrice(new BigDecimal("10.00"))
                .sale(sale)
                .build();

        Product product2 = new Product();
        ProductSale item2 = ProductSale.builder()
                .product(product2)
                .quantity(1)
                .unitPrice(new BigDecimal("20.00"))
                .sale(sale)
                .build();

        sale.setProductSales(List.of(item1, item2));

        saleGenerator.registerStockMovementsFromSale(sale);


        verify(stockMovementService, times(2)).registerOutput(
                any(Product.class),
                anyInt(),
                anyString(),
                any(BigDecimal.class),
                eq(sale)
        );

        verify(stockMovementService).registerOutput(
                product1,
                2,
                "SALE_ORDER_" + saleId,
                new BigDecimal("10.00"),
                sale
        );
    }
}