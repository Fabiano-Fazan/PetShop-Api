package com.petshop.api.service;



import com.petshop.api.domain.Validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateVeterinarianCategoryDto;
import com.petshop.api.dto.request.UpdateVeterinarianCategoryDto;
import com.petshop.api.dto.response.VeterinarianCategoryResponseDto;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.VeterinarianCategory;
import com.petshop.api.model.mapper.VeterinarianCategoryMapper;
import com.petshop.api.repository.VeterinarianCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VeterinarianCategoryService {

    private final VeterinarianCategoryMapper veterinarianCategoryMapper;
    private final VeterinarianCategoryRepository veterinarianCategoryRepository;
    private final ValidatorEntities validatorEntities;


    public VeterinarianCategoryResponseDto getVeterinarianCategoryById(UUID id) {
        return veterinarianCategoryRepository.findById(id)
                .map(veterinarianCategoryMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian category not found with ID: " + id));
    }

    public Page<VeterinarianCategoryResponseDto> getAllVeterinarianCategories(Pageable pageable) {
        return veterinarianCategoryRepository.findAll(pageable)
                .map(veterinarianCategoryMapper::toResponseDto);
    }

    public VeterinarianCategoryResponseDto getByName(String name) {
        return veterinarianCategoryRepository.findByNameContainingIgnoreCase(name)
                .map(veterinarianCategoryMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian category not found with name: " + name));
    }

    @Transactional
    public VeterinarianCategoryResponseDto createVeterinarianCategory(CreateVeterinarianCategoryDto createVeterinarianCategoryDTO) {
        VeterinarianCategory veterinarianCategory = veterinarianCategoryMapper.toEntity(createVeterinarianCategoryDTO);
        return veterinarianCategoryMapper.toResponseDto(veterinarianCategoryRepository.save(veterinarianCategory));
    }

    @Transactional
    public VeterinarianCategoryResponseDto updateVeterinarianCategory(UUID id, UpdateVeterinarianCategoryDto updateVeterinarianCategoryDto){
        VeterinarianCategory veterinarianCategory = validatorEntities.validateVeterinarianCategory(id);
        veterinarianCategoryMapper.updateVeterinarianCategoryFromDTO(updateVeterinarianCategoryDto, veterinarianCategory);
        return veterinarianCategoryMapper.toResponseDto(veterinarianCategoryRepository.save(veterinarianCategory));
    }

    @Transactional
    public void deleteVeterinarianCategory(UUID id){
        if (!veterinarianCategoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Veterinarian category not found with ID: " + id);
        }
        veterinarianCategoryRepository.deleteById(id);
    }
}
