package com.petshop.api.controller;

import com.petshop.api.dto.request.CreateAnimalDto;
import com.petshop.api.dto.request.UpdateAnimalDto;
import com.petshop.api.dto.response.AnimalResponseDto;
import com.petshop.api.service.AnimalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping("/api/v1/animals")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping
    public ResponseEntity<Page<AnimalResponseDto>> getAllAnimals(Pageable pageable) {
        Page<AnimalResponseDto> animals = animalService.getAllAnimals(pageable);
        return ResponseEntity.ok(animals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponseDto> getAnimalById(@PathVariable UUID id) {
        AnimalResponseDto animalById = animalService.getAnimalById(id);
        return ResponseEntity.ok(animalById);
    }

    @GetMapping("/species")
    public ResponseEntity<Page<AnimalResponseDto>> getAnimalsBySpecies(@RequestParam String species, Pageable pageable) {
        Page<AnimalResponseDto> animalsBySpecies = animalService.getAnimalsBySpecies(species, pageable);
        return ResponseEntity.ok(animalsBySpecies);
    }

    @GetMapping("/name")
    public ResponseEntity<Page<AnimalResponseDto>> getAnimalsByName(@RequestParam String name, Pageable pageable) {
        Page<AnimalResponseDto> animalsByName = animalService.getAnimalByNameContainingIgnoreCase(name, pageable);
        return ResponseEntity.ok(animalsByName);
    }

    @PostMapping
    public ResponseEntity<AnimalResponseDto> createAnimal(@Valid @RequestBody CreateAnimalDto animalDTO) {
        AnimalResponseDto createdAnimal = animalService.createAnimal(animalDTO);
        return new ResponseEntity<>(createdAnimal, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AnimalResponseDto> updateAnimal(@PathVariable UUID id, @Valid @RequestBody UpdateAnimalDto updateAnimalDTO) {
        AnimalResponseDto updatedAnimal = animalService.updateAnimal(id, updateAnimalDTO);
        return ResponseEntity.ok(updatedAnimal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable UUID id) {
        animalService.deleteAnimal(id);
        return ResponseEntity.noContent().build();
    }
}