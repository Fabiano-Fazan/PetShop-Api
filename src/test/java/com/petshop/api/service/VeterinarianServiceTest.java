package com.petshop.api.service;

import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateVeterinarianDto;
import com.petshop.api.dto.response.VeterinarianResponseDto;
import com.petshop.api.dto.update.UpdateVeterinarianDto;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.Veterinarian;
import com.petshop.api.model.entities.VeterinarianCategory;
import com.petshop.api.model.mapper.VeterinarianMapper;
import com.petshop.api.repository.MedicalAppointmentRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeterinarianServiceTest {

    @InjectMocks
    private VeterinarianService veterinarianService;

    @Mock
    private VeterinarianRepository veterinarianRepository;

    @Mock
    private MedicalAppointmentRepository medicalAppointmentRepository;

    @Mock
    private VeterinarianCategoryRepository veterinarianCategoryRepository;

    @Mock
    private VeterinarianMapper veterinarianMapper;

    @Mock
    private ValidatorEntities validatorEntities;


    @Test
    @DisplayName("Should return a page of veterinarians")
    void getAllVeterinarians_ShouldReturnPageOfVeterinarians() {

        Pageable pageable = PageRequest.of(0, 10);
        Veterinarian veterinarian = new Veterinarian();
        VeterinarianResponseDto responseDto = new VeterinarianResponseDto();
        Page<Veterinarian> veterinarianPage = new PageImpl<>(List.of(veterinarian));

        when(veterinarianRepository.findAll(pageable)).thenReturn(veterinarianPage);
        when(veterinarianMapper.toResponseDto(veterinarian)).thenReturn(responseDto);

        Page<VeterinarianResponseDto> result = veterinarianService.getAllVeterinarians(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(veterinarianRepository).findAll(pageable);
    }


    @Test
    @DisplayName("Should return veterinarian when ID exists")
    void getVeterinarianById_ShouldReturnVeterinarian_WhenIdExists() {

        UUID id = UUID.randomUUID();
        Veterinarian veterinarian = new Veterinarian();
        veterinarian.setId(id);
        VeterinarianResponseDto responseDto = new VeterinarianResponseDto();

        when(validatorEntities.validate(id, veterinarianRepository, "Veterinarian")).thenReturn(veterinarian);
        when(veterinarianMapper.toResponseDto(veterinarian)).thenReturn(responseDto);

        VeterinarianResponseDto result = veterinarianService.getVeterinarianById(id);

        assertThat(result).isNotNull();
        verify(validatorEntities).validate(id, veterinarianRepository, "Veterinarian");
    }


    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID not exists")
    void getVeterinarianById_ShouldThrowException_WhenIdDoesNotExists() {

        UUID id = UUID.randomUUID();
        String expectedMessage = "Veterinarian not found";

        when(validatorEntities.validate(id, veterinarianRepository, "Veterinarian"))
                .thenThrow(new ResourceNotFoundException(expectedMessage));

        assertThatThrownBy(() -> veterinarianService.getVeterinarianById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(expectedMessage);

        verify(validatorEntities).validate(id, veterinarianRepository, "Veterinarian");
        verifyNoInteractions(veterinarianMapper);
    }


    @Test
    @DisplayName("Should return page of veterinarians when searching by name")
    void getVeterinarianByNameContainingIgnoreCase_ShouldReturnPage_WhenNameExists() {

        String name = "Dr. House";
        Pageable pageable = PageRequest.of(0, 10);
        Veterinarian veterinarian = new Veterinarian();
        VeterinarianResponseDto responseDto = new VeterinarianResponseDto();
        Page<Veterinarian> veterinarianPage = new PageImpl<>(List.of(veterinarian));

        when(veterinarianRepository.findByNameContainingIgnoreCase(name, pageable)).thenReturn(veterinarianPage);
        when(veterinarianMapper.toResponseDto(veterinarian)).thenReturn(responseDto);

        Page<VeterinarianResponseDto> result = veterinarianService.getVeterinarianByNameContainingIgnoreCase(name, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }


    @Test
    @DisplayName("Should create veterinarian successfully")
    void createVeterinarian_ShouldReturnDto_WhenSuccessfully() {

        CreateVeterinarianDto createDto = new CreateVeterinarianDto();
        createDto.setCategoryId(UUID.randomUUID());
        Veterinarian veterinarian = new Veterinarian();
        VeterinarianCategory category = new VeterinarianCategory();
        Veterinarian savedVeterinarian = new Veterinarian();
        savedVeterinarian.setId(UUID.randomUUID());
        VeterinarianResponseDto responseDto = new VeterinarianResponseDto();

        when(veterinarianMapper.toEntity(createDto)).thenReturn(veterinarian);
        when(validatorEntities.validate(createDto.getCategoryId(), veterinarianCategoryRepository, "Veterinarian Category")).thenReturn(category);
        when(veterinarianRepository.save(veterinarian)).thenReturn(savedVeterinarian);
        when(veterinarianMapper.toResponseDto(savedVeterinarian)).thenReturn(responseDto);

        VeterinarianResponseDto result = veterinarianService.createVeterinarian(createDto);

        assertThat(result).isNotNull();
        verify(veterinarianRepository).save(veterinarian);
        assertThat(veterinarian.getCategory()).isEqualTo(category);
    }


    @Test
    @DisplayName("Should update veterinarian successfully")
    void updateVeterinarian_ShouldReturnUpdateDto_WhenSuccessful() {

        UUID id = UUID.randomUUID();
        UpdateVeterinarianDto updateDto = new UpdateVeterinarianDto();
        Veterinarian veterinarian = new Veterinarian();
        veterinarian.setId(id);
        Veterinarian savedVeterinarian = new Veterinarian();
        VeterinarianResponseDto responseDto = new VeterinarianResponseDto();

        when(validatorEntities.validate(id, veterinarianRepository, "Veterinarian")).thenReturn(veterinarian);
        when(veterinarianRepository.save(veterinarian)).thenReturn(savedVeterinarian);
        when(veterinarianMapper.toResponseDto(savedVeterinarian)).thenReturn(responseDto);

        VeterinarianResponseDto result = veterinarianService.updateVeterinarian(id, updateDto);

        assertThat(result).isNotNull();
        verify(veterinarianMapper).updateVeterinarianFromDto(updateDto, veterinarian);
        verify(veterinarianRepository).save(veterinarian);
    }


    @Test
    @DisplayName("Should delete veterinarian when its not in use")
    void deleteVeterinarian_ShouldDelete_WhenNotInUse() {

        UUID id = UUID.randomUUID();
        Veterinarian veterinarian = new Veterinarian();
        veterinarian.setId(id);

        when(validatorEntities.validate(id, veterinarianRepository, "Veterinarian")).thenReturn(veterinarian);
        when(medicalAppointmentRepository.existsByVeterinarian(veterinarian)).thenReturn(false);

        veterinarianService.deleteVeterinarian(id);

        verify(veterinarianRepository).delete(veterinarian);
    }


    @Test
    @DisplayName("Should throw BusinessException when trying to delete veterinarian when its in use")
    void deleteVeterinarian_ShouldThrowException_WhenVeterinarianIsInUse() {

        UUID id = UUID.randomUUID();
        Veterinarian veterinarian = new Veterinarian();
        veterinarian.setId(id);

        when(validatorEntities.validate(id, veterinarianRepository, "Veterinarian")).thenReturn(veterinarian);
        when(medicalAppointmentRepository.existsByVeterinarian(veterinarian)).thenReturn(true);

        assertThatThrownBy(() -> veterinarianService.deleteVeterinarian(id))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cannot delete this veterinarian because it is being used by medical appointments");

        verify(veterinarianRepository, never()).delete(any());
    }
}