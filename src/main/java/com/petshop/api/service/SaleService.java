package com.petshop.api.service;


import com.petshop.api.domain.SaleGenerator;
import com.petshop.api.dto.request.CreateSaleDto;
import com.petshop.api.dto.response.SaleResponseDto;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.*;
import com.petshop.api.model.enums.SaleStatus;
import com.petshop.api.model.mapper.SaleMapper;
import com.petshop.api.repository.ClientRepository;
import com.petshop.api.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;
    private final ClientRepository clientRepository;
    private final StockMovementService stockMovementService;
    private final FinancialService financialService;
    private final SaleGenerator saleGenerator;


    public SaleResponseDto getSaleById(UUID id) {
        return saleRepository.findById(id)
                .map(saleMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with ID: " + id));

    }

    public Page<SaleResponseDto> getAllSales(Pageable pageable) {
        return saleRepository.findAll(pageable)
                .map(saleMapper::toResponseDto);

    }

    @Transactional
    public SaleResponseDto createSale(CreateSaleDto createSaleDTO) {

        Client client = clientRepository.findById(createSaleDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + createSaleDTO.getClientId()));

        Sale newSale = new Sale();
        newSale.setClient(client);
        newSale.setStatus(SaleStatus.COMPLETED);
        newSale.setPaymentType(createSaleDTO.getPaymentType());

        createSaleDTO.getProductSales().forEach(item -> {
            ProductSale productSale = saleGenerator.generateProductSale(item, newSale);
            newSale.getProductSales().add(productSale);
                });
        BigDecimal totalValue = saleGenerator.calculateSaleTotal(createSaleDTO);
        newSale.setTotalValue(totalValue);
        Sale savedSale = saleRepository.save(newSale);
        saleGenerator.registerStockMovementsFromSale(savedSale);

        financialService.createFinancialFromSale(
                savedSale,
                createSaleDTO.getInstallments(),
                createSaleDTO.getIntervalDays()
        );

        return saleMapper.toResponseDto(savedSale);

    }

    @Transactional
    public SaleResponseDto cancelSale(UUID id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with ID: " + id));

        if (sale.getStatus() == SaleStatus.CANCELED) {
            throw new IllegalStateException("This sale is already canceled");
        }else{
            sale.setStatus(SaleStatus.CANCELED);
        }

        Sale canceledSale = saleRepository.save(sale);

        for (ProductSale productSold : canceledSale.getProductSales()) {
            String description = "CANCELLATION_OF_SALE_ORDER_" + canceledSale.getId();

            stockMovementService.registerInput(
                    productSold.getProduct(),
                    productSold.getQuantity(),
                    description,
                    null,
                    null
            );
        }
        for (Financial financialCreated : canceledSale.getFinancial()){
            financialService.deleteFinancial(financialCreated.getId());
        }

        return saleMapper.toResponseDto(canceledSale);

    }
}


