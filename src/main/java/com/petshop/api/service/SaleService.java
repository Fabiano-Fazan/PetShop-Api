package com.petshop.api.service;


import com.petshop.api.domain.sale.SaleCancel;
import com.petshop.api.domain.sale.SaleGenerator;
import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateSaleDto;
import com.petshop.api.dto.response.SaleResponseDto;
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
    private final ClientRepository clientRepository;
    private final SaleMapper saleMapper;
    private final StockMovementService stockMovementService;
    private final FinancialService financialService;
    private final SaleGenerator saleGenerator;
    private final ValidatorEntities validatorEntities;
    private final SaleCancel saleCancel;


    public Page<SaleResponseDto> getSaleByClientNameContainingIgnoreCase(String name, Pageable pageable){
        return saleRepository.findByClientNameContainingIgnoreCase(name,pageable)
                .map(saleMapper::toResponseDto);
    }

    public Page<SaleResponseDto> getAllSales(Pageable pageable) {
        return saleRepository.findAll(pageable)
                .map(saleMapper::toResponseDto);
    }

    public SaleResponseDto getSaleById(UUID id) {
        var sale = validatorEntities.validate(id, saleRepository, "Sale");
        return saleMapper.toResponseDto(sale);

    }

    @Transactional
    public SaleResponseDto createSale(CreateSaleDto dto) {
        Sale newSale = new Sale();
        newSale.setClient(validatorEntities.validate(dto.getClientId(), clientRepository, "Client"));
        newSale.setStatus(SaleStatus.COMPLETED);
        newSale.setPaymentType(dto.getPaymentType());
        newSale.setNotes(dto.getNotes());
        dto.getProductSales().forEach(item -> {
            var productSale = saleGenerator.generateProductSale(item, newSale);
            newSale.getProductSales().add(productSale);
        });
        BigDecimal totalValue = saleGenerator.calculateSaleTotal(dto);
        newSale.setTotalValue(totalValue);
        var savedSale = saleRepository.save(newSale);
        saleGenerator.registerStockMovementsFromSale(savedSale);
        financialService.createFinancialFromSale(
                savedSale,
                dto.getInstallments(),
                dto.getIntervalDays()
        );
        return saleMapper.toResponseDto(savedSale);
    }

    @Transactional
    public SaleResponseDto cancelSale(UUID id) {
        var sale = validatorEntities.validate(id, saleRepository, "Sale");
        saleCancel.cancel(sale);
        returnItemsToStock(sale);
        var canceledSale = saleRepository.save(sale);
        return saleMapper.toResponseDto(canceledSale);
    }

    private void returnItemsToStock(Sale sale){
        sale.getProductSales().forEach(productSold -> {
            String description = "CANCELLATION_OF_SALE_ORDER_" + sale.getId();

            stockMovementService.registerInput(
                    productSold.getProduct(),
                    productSold.getQuantity(),
                    description,
                    null,
                    productSold.getUnitPrice(),
                    sale
            );
        });
    }
}



