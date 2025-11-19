package com.petshop.api.service;

import com.petshop.api.dto.request.CreateProductSaleDto;
import com.petshop.api.dto.request.CreateSaleDto;
import com.petshop.api.dto.response.SaleResponseDto;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.Client;
import com.petshop.api.model.entities.Product;
import com.petshop.api.model.entities.ProductSale;
import com.petshop.api.model.entities.Sale;

import com.petshop.api.model.enums.SaleStatus;
import com.petshop.api.model.mapper.SaleMapper;
import com.petshop.api.repository.ClientRepository;
import com.petshop.api.repository.ProductRepository;
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
    private final ProductRepository productRepository;
    private final ClientRepository clientRepository;
    private final StockMovementService stockMovementService;
    private final FinancialService financialService;

    @Transactional
    public SaleResponseDto createSale(CreateSaleDto createSaleDTO) {
        Client client = clientRepository.findById(createSaleDTO.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + createSaleDTO.getClientId()));

        Sale newSale = new Sale();

        newSale.setClient(client);
        newSale.setStatus(SaleStatus.COMPLETED);

        BigDecimal totalValue = BigDecimal.ZERO;

        for (CreateProductSaleDto productSaleDTO : createSaleDTO.getProductSales()) {
            Product product = productRepository.findWithLockById(productSaleDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID:" + productSaleDTO.getProductId()));

            ProductSale productSale = new ProductSale();
            productSale.setProduct(product);
            productSale.setQuantity(productSaleDTO.getQuantity());
            productSale.setUnitPrice(productSaleDTO.getPrice());
            productSale.setSale(newSale);
            newSale.getProductSales().add(productSale);

            totalValue = totalValue.add(productSaleDTO.getPrice().multiply(BigDecimal.valueOf(productSaleDTO.getQuantity())));
        }
        newSale.setTotalValue(totalValue);

        Sale savedSale = saleRepository.save(newSale);

        for (ProductSale productSold : savedSale.getProductSales()) {
            String description = "SALE_ORDER_" + savedSale.getId();
            stockMovementService.registerOutput(
                    productSold.getProduct(),
                    productSold.getQuantity(),
                    description,
                    productSold.getUnitPrice(),
                    savedSale
            );
        }

        return saleMapper.toResponseDto(savedSale);
    }

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
    public SaleResponseDto cancelSale(UUID id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with ID: " + id));

        if (sale.getStatus() == SaleStatus.CANCELED) {
            throw new IllegalStateException("This sale is already canceled");
        }

        sale.setStatus(SaleStatus.CANCELED);
        Sale canceledSale = saleRepository.save(sale);

        for (ProductSale productSold : canceledSale.getProductSales()) {
            String description = "CANCELATION_OF_SALE_ORDER_" + canceledSale.getId();

            stockMovementService.registerInput(
                    productSold.getProduct(),
                    productSold.getQuantity(),
                    description,
                    null,
                    null
            );
        }
        return saleMapper.toResponseDto(canceledSale);
    }
}


