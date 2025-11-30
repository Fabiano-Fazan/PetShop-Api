package com.petshop.api.controller;

import com.petshop.api.dto.request.CreateSaleDto;
import com.petshop.api.dto.response.SaleResponseDto;
import com.petshop.api.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sale")
public class SaleController {
    private  final SaleService saleService;

    @GetMapping
    public ResponseEntity<Page<SaleResponseDto>> getAllSales(Pageable pageable){
        Page<SaleResponseDto> sales = saleService.getAllSales(pageable);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/id")
    public  ResponseEntity<SaleResponseDto> findSaleById(@PathVariable UUID id){
        SaleResponseDto sale = saleService.getSaleById(id);
        return ResponseEntity.ok(sale);
    }

    @PostMapping
    public ResponseEntity<SaleResponseDto> createSale(@Valid @RequestBody CreateSaleDto createSaleDTO){
        SaleResponseDto createdSale = saleService.createSale(createSaleDTO);
        return new ResponseEntity<>(createdSale, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<SaleResponseDto> cancelSale(@PathVariable UUID id){
        SaleResponseDto canceledSale = saleService.cancelSale(id);
        return ResponseEntity.ok(canceledSale);
    }
}
