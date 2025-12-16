package com.petshop.api.service;



import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateVeterinarianCategoryDto;
import com.petshop.api.dto.request.UpdateVeterinarianCategoryDto;
import com.petshop.api.dto.response.VeterinarianCategoryResponseDto;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.VeterinarianCategory;
import com.petshop.api.model.mapper.VeterinarianCategoryMapper;
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
public class VeterinarianCategoryService {

    private final VeterinarianCategoryMapper veterinarianCategoryMapper;
    private final VeterinarianCategoryRepository veterinarianCategoryRepository;
    private final ValidatorEntities validatorEntities;
    private final VeterinarianRepository veterinarianRepository;


    public VeterinarianCategoryResponseDto getVeterinarianCategoryById(UUID id) {
        return veterinarianCategoryRepository.findById(id)
                .map(veterinarianCategoryMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Veterinarian category not found"));
    }

    public Page<VeterinarianCategoryResponseDto> getAllVeterinarianCategories(Pageable pageable) {
        return veterinarianCategoryRepository.findAll(pageable)
                .map(veterinarianCategoryMapper::toResponseDto);
    }

    public Page<VeterinarianCategoryResponseDto> getVeterinarianCategoryByNameContainingIgnoreCase(String name, Pageable pageable) {
        return veterinarianCategoryRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(veterinarianCategoryMapper::toResponseDto);
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
            throw new ResourceNotFoundException("Veterinarian category not found");
        }
        if (veterinarianRepository.existsByCategoryId(id)) {
            throw new BusinessException("Cannot delete this veterinarian category because it is being used by veterinarians");
        }
        veterinarianCategoryRepository.deleteById(id);
    }
}
