package com.petshop.api.controller;

import com.petshop.api.dto.request.CreateFinancialDto;
import com.petshop.api.dto.response.FinancialResponseDto;
import com.petshop.api.service.FinancialService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/financial")
public class FinancialController {
    private final FinancialService financialService;

    @GetMapping
    public ResponseEntity<Page<FinancialResponseDto>> getAllFinancial(Pageable pageable){
        Page<FinancialResponseDto> financials = financialService.getAllFinancial(pageable);
        return ResponseEntity.ok(financials);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialResponseDto> getFinancialById(@PathVariable UUID id){
        FinancialResponseDto financial = financialService.getFinancialById(id);
        return ResponseEntity.ok(financial);
    }

    @GetMapping("/client/{name}")
    public ResponseEntity<Page<FinancialResponseDto>> getByClientName(@PathVariable String name, Pageable pageable){
        Page<FinancialResponseDto> financials = financialService.getByClientNameContainingIgnoreCase(name, pageable);
        return ResponseEntity.ok(financials);
    }

    @PostMapping
    public ResponseEntity<FinancialResponseDto> createFinancial(@RequestBody CreateFinancialDto createFinancialDTO){
        FinancialResponseDto createdFinancial = financialService.createManualFinancial(createFinancialDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancial(@PathVariable UUID id){
        financialService.deleteFinancial(id);
        return ResponseEntity.noContent().build();
    }
}
