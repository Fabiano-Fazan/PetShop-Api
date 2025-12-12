package com.petshop.api.controller;

import com.petshop.api.dto.request.CreateStockMovementDto;
import com.petshop.api.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stock")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @PostMapping("/input/{id}")
    public ResponseEntity<Void> giveInputStock(@PathVariable UUID id, @RequestBody  CreateStockMovementDto createStockMovementDTO){
        stockMovementService.registerInput(id, createStockMovementDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/output/{id}")
    public ResponseEntity<Void> giveOutputStock(@PathVariable UUID id,@RequestBody  CreateStockMovementDto createStockMovementDTO){
        stockMovementService.registerOutput(id, createStockMovementDTO);
        return ResponseEntity.ok().build();
    }
}
