package com.petshop.api.controller;

import com.petshop.api.dto.request.CreateVeterinarianCategoryDto;
import com.petshop.api.dto.request.UpdateVeterinarianCategoryDto;
import com.petshop.api.dto.response.VeterinarianCategoryResponseDto;
import com.petshop.api.service.VeterinarianCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/veterinarian-categories")
@RequiredArgsConstructor
public class VeterinarianCategoryController {
    private final VeterinarianCategoryService veterinarianCategoryService;

    @GetMapping
    public ResponseEntity<Page<VeterinarianCategoryResponseDto>> getAllVeterinarianCategories(Pageable pageable) {
        Page<VeterinarianCategoryResponseDto> allVeterinarianCategories = veterinarianCategoryService.getAllVeterinarianCategories(pageable);
        return ResponseEntity.ok(allVeterinarianCategories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeterinarianCategoryResponseDto> getVeterinarianCategoryById(@PathVariable UUID id) {
        VeterinarianCategoryResponseDto veterinarianCategoryById = veterinarianCategoryService.getVeterinarianCategoryById(id);
        return ResponseEntity.ok(veterinarianCategoryById);
    }

    @GetMapping("/name")
    public ResponseEntity<VeterinarianCategoryResponseDto> getByName(@RequestParam String name) {
        VeterinarianCategoryResponseDto veterinarianCategoryByName = veterinarianCategoryService.getVeterinarianCategoryByName(name);
        return ResponseEntity.ok(veterinarianCategoryByName);
    }

    @PostMapping
    public ResponseEntity<VeterinarianCategoryResponseDto> createVeterinarianCategory(@Valid @RequestBody CreateVeterinarianCategoryDto createVeterinarianCategoryDTO){
        VeterinarianCategoryResponseDto createdVeterinarianCategory = veterinarianCategoryService.createVeterinarianCategory(createVeterinarianCategoryDTO);
        return new ResponseEntity<>(createdVeterinarianCategory, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VeterinarianCategoryResponseDto> updateVeterinarianCategory(@PathVariable UUID id, @Valid @RequestBody UpdateVeterinarianCategoryDto updateVeterinarianCategoryDTO) {
        VeterinarianCategoryResponseDto updatedVeterinarianCategory = veterinarianCategoryService.updateVeterinarianCategory(id, updateVeterinarianCategoryDTO);
        return ResponseEntity.ok(updatedVeterinarianCategory);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeterinarianCategory(@PathVariable UUID id) {
        veterinarianCategoryService.deleteVeterinarianCategory(id);
        return ResponseEntity.noContent().build();
    }

}
