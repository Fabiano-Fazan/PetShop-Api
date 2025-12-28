package com.petshop.api.controller;

import com.petshop.api.dto.request.CreateMonetaryType;
import com.petshop.api.dto.request.UpdateMonetaryTypeDto;
import com.petshop.api.dto.response.MonetaryTypeResponseDto;
import com.petshop.api.service.MonetaryTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/monetary-types")
@RequiredArgsConstructor
public class MonetaryTypeController {

    private final MonetaryTypeService monetaryTypeService;

    @GetMapping
    public ResponseEntity<Page<MonetaryTypeResponseDto>> getAllMonetaryTypes(Pageable pageable) {
        Page<MonetaryTypeResponseDto> allMonetaryTypes = monetaryTypeService.getAllMonetaryTypes(pageable);
        return ResponseEntity.ok(allMonetaryTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonetaryTypeResponseDto> getMonetaryTypeById(@PathVariable UUID id){
        MonetaryTypeResponseDto monetaryTypeById = monetaryTypeService.getMonetaryTypeById(id);
        return ResponseEntity.ok(monetaryTypeById);
    }

    @GetMapping("/name")
    public ResponseEntity<Page<MonetaryTypeResponseDto>> getByName(@RequestParam String name, Pageable pageable){
        Page<MonetaryTypeResponseDto> monetaryTypeByName = monetaryTypeService.getMonetaryTypeByNameContainingIgnoreCase(name, pageable);
        return ResponseEntity.ok(monetaryTypeByName);
    }

    @PostMapping
    public ResponseEntity<MonetaryTypeResponseDto> createMonetaryType(@RequestBody CreateMonetaryType createMonetaryTypeDTO){
        MonetaryTypeResponseDto createdMonetaryType = monetaryTypeService.createMonetaryType(createMonetaryTypeDTO);
        return new ResponseEntity<>(createdMonetaryType, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MonetaryTypeResponseDto> updateMonetaryType(@PathVariable UUID id, @RequestBody UpdateMonetaryTypeDto updateMonetaryTypeDto){
        MonetaryTypeResponseDto updatedMonetaryType = monetaryTypeService.updateMonetaryType(id, updateMonetaryTypeDto);
        return ResponseEntity.ok(updatedMonetaryType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonetaryType(@PathVariable UUID id){
        monetaryTypeService.deleteMonetaryType(id);
        return ResponseEntity.noContent().build();
    }
}
