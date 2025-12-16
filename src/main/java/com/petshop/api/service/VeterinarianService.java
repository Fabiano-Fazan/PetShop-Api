package com.petshop.api.service;

import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateVeterinarianDto;
import com.petshop.api.dto.request.UpdateVeterinarianDto;
import com.petshop.api.dto.response.VeterinarianResponseDto;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.Veterinarian;
import com.petshop.api.model.mapper.VeterinarianMapper;
import com.petshop.api.repository.MedicalAppointmentRepository;
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
    private final ValidatorEntities validatorEntities;
    private final MedicalAppointmentRepository medicalAppointmentRepository;


    public Page<VeterinarianResponseDto> getAllVeterinarians(Pageable pageable){
        return veterinarianRepository.findAll(pageable)
        .map(veterinarianMapper::toResponseDto);
    }

    public VeterinarianResponseDto getVeterinarianById(UUID id){
        return veterinarianRepository.findById(id)
                .map(veterinarianMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian not found"));
    }

    public Page<VeterinarianResponseDto> getVeterinarianByNameContainingIgnoreCase(String name, Pageable pageable){
        return veterinarianRepository.findByNameContainingIgnoreCase(name,pageable)
                .map(veterinarianMapper::toResponseDto);
    }

    @Transactional
    public VeterinarianResponseDto createVeterinarian(CreateVeterinarianDto createVeterinarianDTO){
        Veterinarian veterinarian = veterinarianMapper.toEntity(createVeterinarianDTO);
        veterinarian.setCategory(validatorEntities.validateVeterinarianCategory(createVeterinarianDTO.getCategoryId()));
        return veterinarianMapper.toResponseDto(veterinarianRepository.save(veterinarian));
    }

    @Transactional
    public VeterinarianResponseDto updateVeterinarian(UUID id, UpdateVeterinarianDto updateVeterinarianDTO){
        Veterinarian veterinarian = validatorEntities.validateVeterinarian(id);
        veterinarianMapper.updateVeterinarianFromDTO(updateVeterinarianDTO, veterinarian);
        return veterinarianMapper.toResponseDto(veterinarianRepository.save(veterinarian));
    }

    @Transactional
    public void deleteVeterinarian(UUID id){
        if(!veterinarianRepository.existsById(id)){
            throw new ResourceNotFoundException("Veterinarian not found");
        }
        if(medicalAppointmentRepository.existsByVeterinarianId(id)){
            throw new BusinessException("Cannot delete veterinarian with scheduled medical appointments.");
        }
        veterinarianRepository.deleteById(id);
    }
}
