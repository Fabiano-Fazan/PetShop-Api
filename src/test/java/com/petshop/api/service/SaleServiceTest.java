package com.petshop.api.service;

import com.petshop.api.domain.sale.SaleCancel;
import com.petshop.api.domain.sale.SaleGenerator;
import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateProductSaleDto;
import com.petshop.api.dto.request.CreateSaleDto;
import com.petshop.api.dto.response.SaleResponseDto;
import com.petshop.api.model.entities.Client;
import com.petshop.api.model.entities.Product;
import com.petshop.api.model.entities.ProductSale;
import com.petshop.api.model.entities.Sale;
import com.petshop.api.model.enums.SalePaymentType;
import com.petshop.api.model.enums.SaleStatus;
import com.petshop.api.model.mapper.SaleMapper;
import com.petshop.api.repository.ClientRepository;
import com.petshop.api.repository.SaleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @InjectMocks
    private SaleService saleService;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private SaleMapper saleMapper;

    @Mock
    private StockMovementService stockMovementService;

    @Mock
    private FinancialService financialService;

    @Mock
    private SaleGenerator saleGenerator;

    @Mock
    private ValidatorEntities validatorEntities;

    @Mock
    private SaleCancel saleCancel;


    @Test
    @DisplayName("Should return sale when ID exists")
    void getSaleById_ShouldReturnSale() {

        UUID id = UUID.randomUUID();
        Sale sale = new Sale();
        sale.setId(id);
        SaleResponseDto responseDto = new SaleResponseDto();

        when(validatorEntities.validate(id, saleRepository, "Sale")).thenReturn(sale);
        when(saleMapper.toResponseDto(sale)).thenReturn(responseDto);

        SaleResponseDto result = saleService.getSaleById(id);

        assertThat(result).isNotNull();
        verify(validatorEntities).validate(id, saleRepository, "Sale");
    }


    @Test
    @DisplayName("Should return page of sales")
    void getAllSales_ShouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);
        Sale sale = new Sale();
        SaleResponseDto responseDto = new SaleResponseDto();
        Page<Sale> page = new PageImpl<>(List.of(sale));

        when(saleRepository.findAll(pageable)).thenReturn(page);
        when(saleMapper.toResponseDto(sale)).thenReturn(responseDto);

        Page<SaleResponseDto> result = saleService.getAllSales(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }


    @Test
    @DisplayName("Should return page of sales by client name")
    void getSaleByClientNameContainingIgnoreCase_ShouldReturnPage() {

        String name = "Maria";
        Pageable pageable = PageRequest.of(0, 10);
        Sale sale = new Sale();
        SaleResponseDto responseDto = new SaleResponseDto();
        Page<Sale> page = new PageImpl<>(List.of(sale));

        when(saleRepository.findByClientNameContainingIgnoreCase(name, pageable)).thenReturn(page);
        when(saleMapper.toResponseDto(sale)).thenReturn(responseDto);

        Page<SaleResponseDto> result = saleService.getSaleByClientNameContainingIgnoreCase(name, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }


    @Test
    @DisplayName("Should create sale successfully, generating stock movements and financial records")
    void createSale_ShouldOrchestrateCreationCorrectly() {

        CreateSaleDto createDto = new CreateSaleDto();
        createDto.setClientId(UUID.randomUUID());
        createDto.setPaymentType(SalePaymentType.CASH);
        createDto.setInstallments(1);
        createDto.setIntervalDays(0);
        CreateProductSaleDto itemDto = new CreateProductSaleDto();
        itemDto.setProductId(UUID.randomUUID());
        itemDto.setQuantity(2);
        createDto.setProductSales(List.of(itemDto));
        Client client = new Client();
        Sale sale = new Sale();
        sale.setProductSales(new ArrayList<>());
        ProductSale productSale = new ProductSale();
        Sale savedSale = new Sale();
        savedSale.setId(UUID.randomUUID());
        savedSale.setTotalValue(new BigDecimal("100.00"));
        SaleResponseDto responseDto = new SaleResponseDto();

        when(validatorEntities.validate(createDto.getClientId(), clientRepository, "Client")).thenReturn(client);
        when(saleGenerator.generateProductSale(any(), any(Sale.class))).thenReturn(productSale);
        when(saleGenerator.calculateSaleTotal(createDto)).thenReturn(new BigDecimal("100.00"));
        when(saleRepository.save(any(Sale.class))).thenReturn(savedSale);
        when(saleMapper.toResponseDto(savedSale)).thenReturn(responseDto);

        SaleResponseDto result = saleService.createSale(createDto);

        assertThat(result).isNotNull();
        verify(validatorEntities).validate(createDto.getClientId(), clientRepository, "Client");
        verify(saleGenerator, times(1)).generateProductSale(eq(itemDto), any(Sale.class));
        verify(saleGenerator).calculateSaleTotal(createDto);
        verify(saleRepository).save(any(Sale.class));
        verify(saleGenerator).registerStockMovementsFromSale(savedSale);
        verify(financialService).createFinancialFromSale(savedSale, createDto.getInstallments(), createDto.getIntervalDays());
    }


    @Test
    @DisplayName("Should cancel sale and return items to stock")
    void cancelSale_ShouldCancelAndReturnStock() {

        UUID saleId = UUID.randomUUID();
        Sale sale = new Sale();
        sale.setId(saleId);
        sale.setStatus(SaleStatus.COMPLETED);
        Product product = new Product();
        ProductSale item = new ProductSale();
        item.setProduct(product);
        item.setQuantity(5);
        item.setUnitPrice(new BigDecimal("10.00"));
        sale.setProductSales(List.of(item));
        Sale canceledSale = new Sale();
        canceledSale.setId(saleId);
        canceledSale.setStatus(SaleStatus.CANCELED);
        SaleResponseDto responseDto = new SaleResponseDto();

        when(validatorEntities.validate(saleId, saleRepository, "Sale")).thenReturn(sale);
        when(saleRepository.save(sale)).thenReturn(canceledSale);
        when(saleMapper.toResponseDto(canceledSale)).thenReturn(responseDto);

        SaleResponseDto result = saleService.cancelSale(saleId);

        assertThat(result).isNotNull();
        verify(saleCancel).cancel(sale);
        verify(stockMovementService).registerInput(
                eq(product),
                eq(5),
                contains("CANCELLATION_OF_SALE_ORDER_"),
                eq(null),
                eq(new BigDecimal("10.00")),
                eq(sale)
        );

        verify(saleRepository).save(sale);
    }
}