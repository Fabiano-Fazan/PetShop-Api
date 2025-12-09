package com.petshop.api.controller;

import com.petshop.api.dto.request.CreateFinancialDto;
import com.petshop.api.dto.response.FinancialResponseDto;
import com.petshop.api.service.FinancialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/name")
    public ResponseEntity<Page<FinancialResponseDto>> getByClientName(@RequestParam String name, Pageable pageable){
        Page<FinancialResponseDto> financials = financialService.getByClientName(name, pageable);
        return ResponseEntity.ok(financials);
    }

    @PostMapping
    public ResponseEntity<List<FinancialResponseDto>> createFinancial(@RequestBody @Valid CreateFinancialDto createFinancialDto){
        List<FinancialResponseDto> financials = financialService.createManualFinancial(createFinancialDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(financials);
    }

    @PatchMapping("/{id}/payment")
    public ResponseEntity<FinancialResponseDto> markAsPaid(@PathVariable UUID id, @RequestParam String paymentDescription){
        FinancialResponseDto paidFinancial = financialService.markAsPaidFinancial(id, paymentDescription);
        return ResponseEntity.ok(paidFinancial);
    }

    @PatchMapping("/{id}/refund")
    public ResponseEntity<FinancialResponseDto> refundFinancial(@PathVariable UUID id, @RequestParam String refundDescription) {
        FinancialResponseDto refundedFinancial = financialService.refundFinancial(id, refundDescription);
        return ResponseEntity.ok(refundedFinancial);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancial(@PathVariable UUID id){
        financialService.deleteFinancial(id);
        return ResponseEntity.noContent().build();
    }
}
