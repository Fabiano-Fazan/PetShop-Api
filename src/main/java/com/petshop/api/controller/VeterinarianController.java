package com.petshop.api.controller;

import com.petshop.api.dto.request.CreateVeterinarianDto;
import com.petshop.api.dto.request.UpdateVeterinarianDto;
import com.petshop.api.dto.response.VeterinarianResponseDto;
import com.petshop.api.service.VeterinarianService;
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
@RequestMapping("/api/v1/veterinarians")
public class VeterinarianController {
    private final VeterinarianService veterinarianService;

    @GetMapping
    public ResponseEntity<Page<VeterinarianResponseDto>> getAllVeterinarians(Pageable pageable){
        Page<VeterinarianResponseDto> allVeterinarians = veterinarianService.getAllVeterinarians(pageable);
        return ResponseEntity.ok(allVeterinarians);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeterinarianResponseDto> getVeterinarianById(@PathVariable UUID id){
        VeterinarianResponseDto veterinarianById = veterinarianService.getVeterinarianById(id);
        return ResponseEntity.ok(veterinarianById);
    }

    @GetMapping("/name")
    public ResponseEntity<Page<VeterinarianResponseDto>> getVeterinarianByName(@RequestParam String name, Pageable pageable){
        Page<VeterinarianResponseDto> veterinarianByName = veterinarianService.getVeterinarianByNameContainingIgnoreCase(name,pageable);
        return ResponseEntity.ok(veterinarianByName);
    }

    @PostMapping
    public ResponseEntity<VeterinarianResponseDto> createVeterinarian(@Valid @RequestBody CreateVeterinarianDto createVeterinarianDTO){
        VeterinarianResponseDto createdVeterinarian = veterinarianService.createVeterinarian(createVeterinarianDTO);
        return new ResponseEntity<>(createdVeterinarian, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VeterinarianResponseDto> updateVeterinarian(@PathVariable UUID id, @Valid @RequestBody UpdateVeterinarianDto updateVeterinarianDTO){
        VeterinarianResponseDto updatedVeterinarian = veterinarianService.updateVeterinarian(id, updateVeterinarianDTO);
        return ResponseEntity.ok(updatedVeterinarian);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeterinarian(@PathVariable UUID id){
        veterinarianService.deleteVeterinarian(id);
        return ResponseEntity.noContent().build();
    }

}
