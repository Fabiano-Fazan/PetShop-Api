package com.petshop.api.service;

import com.petshop.api.dto.request.CreateVeterinarianDto;
import com.petshop.api.dto.request.UpdateVeterinarianDto;
import com.petshop.api.dto.response.VeterinarianResponseDto;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.Veterinarian;
import com.petshop.api.model.entities.VeterinarianCategory;
import com.petshop.api.model.mapper.VeterinarianMapper;
import com.petshop.api.repository.VeterinarianCategoryRepository;
import com.petshop.api.repository.VeterinarianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VeterinarianService {
    private final VeterinarianRepository veterinarianRepository;
    private final VeterinarianMapper veterinarianMapper;
    private final VeterinarianCategoryRepository veterinarianCategoryRepository;


    public Page<VeterinarianResponseDto> getAllVeterinarians(Pageable pageable){
        return veterinarianRepository.findAll(pageable)
        .map(veterinarianMapper::toResponseDto);
    }

    public VeterinarianResponseDto getVeterinarianById(UUID id){
        return veterinarianRepository.findById(id)
                .map(veterinarianMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found with ID: " + id));
    }

    public Page<VeterinarianResponseDto> getVeterinarianByNameContainingIgnoreCase(String name, Pageable pageable){
        return veterinarianRepository.findAllByNameContainingIgnoreCase(name,pageable)
                .map(veterinarianMapper::toResponseDto);
    }

    @Transactional
    public VeterinarianResponseDto createVeterinarian(CreateVeterinarianDto createVeterinarianDTO){
        Veterinarian veterinarian = veterinarianMapper.toEntity(createVeterinarianDTO);
        VeterinarianCategory category = veterinarianCategoryRepository.findByNameContainingIgnoreCase(createVeterinarianDTO.getCategory())
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian Category not found with name: " + createVeterinarianDTO.getCategory()));
        veterinarian.setCategory(category);
        Veterinarian savedVeterinarian = veterinarianRepository.save(veterinarian);
        return veterinarianMapper.toResponseDto(savedVeterinarian);
    }

    @Transactional
    public VeterinarianResponseDto updateVeterinarian(UUID id, UpdateVeterinarianDto updateVeterinarianDTO){
        Veterinarian veterinarian = veterinarianRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found with ID: " + id));
        veterinarianMapper.updateVeterinarianFromDTO(updateVeterinarianDTO, veterinarian);
        veterinarianRepository.save(veterinarian);
        return veterinarianMapper.toResponseDto(veterinarian);
    }

    @Transactional
    public void deleteVeterinarian(UUID id){
        if(!veterinarianRepository.existsById(id)){
            throw new ResourceNotFoundException("Veterinarian not found with ID: " + id);
            }
        veterinarianRepository.deleteById(id);
    }
}
