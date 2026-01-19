package com.petshop.api.service;

import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateVeterinarianCategoryDto;
import com.petshop.api.dto.response.VeterinarianCategoryResponseDto;
import com.petshop.api.dto.update.UpdateVeterinarianCategoryDto;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.VeterinarianCategory;
import com.petshop.api.model.mapper.VeterinarianCategoryMapper;
import com.petshop.api.repository.VeterinarianCategoryRepository;
import com.petshop.api.repository.VeterinarianRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeterinarianCategoryServiceTest {

    @InjectMocks
    private VeterinarianCategoryService veterinarianCategoryService;

    @Mock
    private VeterinarianCategoryRepository veterinarianCategoryRepository;

    @Mock
    private VeterinarianRepository veterinarianRepository;

    @Mock
    private VeterinarianCategoryMapper veterinarianCategoryMapper;

    @Mock
    private ValidatorEntities validatorEntities;

    @Test
    @DisplayName("Should return a page of veterinarian categories")
    void getAllVeterinarianCategories_ShouldReturnPageOfCategories() {

        Pageable pageable = PageRequest.of(0, 10);
        VeterinarianCategory category = new VeterinarianCategory();
        VeterinarianCategoryResponseDto responseDto = new VeterinarianCategoryResponseDto();
        Page<VeterinarianCategory> categoryPage = new PageImpl<>(List.of(category));

        when(veterinarianCategoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(veterinarianCategoryMapper.toResponseDto(category)).thenReturn(responseDto);

        Page<VeterinarianCategoryResponseDto> result = veterinarianCategoryService.getAllVeterinarianCategories(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(veterinarianCategoryRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return veterinarian category when ID exists")
    void getVeterinarianCategoryById_ShouldReturnCategory_WhenIdExists() {

        UUID id = UUID.randomUUID();
        VeterinarianCategory category = new VeterinarianCategory();
        category.setId(id);
        VeterinarianCategoryResponseDto responseDto = new VeterinarianCategoryResponseDto();

        when(validatorEntities.validate(id, veterinarianCategoryRepository, "Veterinarian Category")).thenReturn(category);
        when(veterinarianCategoryMapper.toResponseDto(category)).thenReturn(responseDto);

        VeterinarianCategoryResponseDto result = veterinarianCategoryService.getVeterinarianCategoryById(id);

        assertThat(result).isNotNull();
        verify(validatorEntities).validate(id, veterinarianCategoryRepository, "Veterinarian Category");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID not exists")
    void getVeterinarianCategoryById_ShouldThrowException_WhenIdDoesNotExists() {

        UUID id = UUID.randomUUID();
        String expectedMessage = "Veterinarian Category not found";

        when(validatorEntities.validate(id, veterinarianCategoryRepository, "Veterinarian Category"))
                .thenThrow(new ResourceNotFoundException(expectedMessage));

        assertThatThrownBy(() -> veterinarianCategoryService.getVeterinarianCategoryById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(expectedMessage);

        verify(validatorEntities).validate(id, veterinarianCategoryRepository, "Veterinarian Category");
        verifyNoInteractions(veterinarianCategoryMapper);
    }

    @Test
    @DisplayName("Should return page of veterinarian categories when searching by name")
    void getVeterinarianCategoryByName_ShouldReturnPage_WhenNameExists() {

        String name = "Surgery";
        Pageable pageable = PageRequest.of(0, 10);
        VeterinarianCategory category = new VeterinarianCategory();
        VeterinarianCategoryResponseDto responseDto = new VeterinarianCategoryResponseDto();
        Page<VeterinarianCategory> categoryPage = new PageImpl<>(List.of(category));

        when(veterinarianCategoryRepository.findByNameContainingIgnoreCase(name, pageable)).thenReturn(categoryPage);
        when(veterinarianCategoryMapper.toResponseDto(category)).thenReturn(responseDto);

        Page<VeterinarianCategoryResponseDto> result = veterinarianCategoryService.getVeterinarianCategoryByNameContainingIgnoreCase(name, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("Should create veterinarian category successfully")
    void createVeterinarianCategory_ShouldReturnDto_WhenSuccessful() {

        CreateVeterinarianCategoryDto createDto = new CreateVeterinarianCategoryDto();
        VeterinarianCategory category = new VeterinarianCategory();
        VeterinarianCategory savedCategory = new VeterinarianCategory();
        savedCategory.setId(UUID.randomUUID());
        VeterinarianCategoryResponseDto responseDto = new VeterinarianCategoryResponseDto();

        when(veterinarianCategoryMapper.toEntity(createDto)).thenReturn(category);
        when(veterinarianCategoryRepository.save(category)).thenReturn(savedCategory);
        when(veterinarianCategoryMapper.toResponseDto(savedCategory)).thenReturn(responseDto);

        VeterinarianCategoryResponseDto result = veterinarianCategoryService.createVeterinarianCategory(createDto);

        assertThat(result).isNotNull();
        verify(veterinarianCategoryRepository).save(category);
    }

    @Test
    @DisplayName("Should update veterinarian category successfully")
    void updateVeterinarianCategory_ShouldReturnUpdateDto_WhenSuccessful() {

        UUID id = UUID.randomUUID();
        UpdateVeterinarianCategoryDto updateDto = new UpdateVeterinarianCategoryDto();
        VeterinarianCategory category = new VeterinarianCategory();
        category.setId(id);
        VeterinarianCategory savedCategory = new VeterinarianCategory();
        VeterinarianCategoryResponseDto responseDto = new VeterinarianCategoryResponseDto();

        when(validatorEntities.validate(id, veterinarianCategoryRepository, "Veterinarian Category")).thenReturn(category);
        when(veterinarianCategoryRepository.save(category)).thenReturn(savedCategory);
        when(veterinarianCategoryMapper.toResponseDto(savedCategory)).thenReturn(responseDto);

        VeterinarianCategoryResponseDto result = veterinarianCategoryService.updateVeterinarianCategory(id, updateDto);

        assertThat(result).isNotNull();
        verify(veterinarianCategoryMapper).updateVeterinarianCategoryFromDto(updateDto, category);
        verify(veterinarianCategoryRepository).save(category);
    }

    @Test
    @DisplayName("Should delete veterinarian category when it is not in use")
    void deleteVeterinarianCategory_ShouldDelete_WhenNotInUse() {
        UUID id = UUID.randomUUID();
        VeterinarianCategory category = new VeterinarianCategory();
        category.setId(id);

        when(validatorEntities.validate(id, veterinarianCategoryRepository, "Veterinarian Category")).thenReturn(category);
        when(veterinarianRepository.existsByCategory(category)).thenReturn(false);

        veterinarianCategoryService.deleteVeterinarianCategory(id);

        verify(veterinarianCategoryRepository).delete(category);
    }

    @Test
    @DisplayName("Should throw BusinessException when trying to delete category that is in use")
    void deleteVeterinarianCategory_ShouldThrowException_WhenCategoryIsInUse() {

        UUID id = UUID.randomUUID();
        VeterinarianCategory category = new VeterinarianCategory();
        category.setId(id);

        when(validatorEntities.validate(id, veterinarianCategoryRepository, "Veterinarian Category")).thenReturn(category);
        when(veterinarianRepository.existsByCategory(category)).thenReturn(true);

        assertThatThrownBy(() -> veterinarianCategoryService.deleteVeterinarianCategory(id))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cannot delete this category because it is being used by veterinarians");

        verify(veterinarianCategoryRepository, never()).delete(any());
    }
}