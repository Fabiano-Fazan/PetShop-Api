package com.petshop.api.service;

import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateMonetaryType;
import com.petshop.api.dto.response.MonetaryTypeResponseDto;
import com.petshop.api.dto.update.UpdateMonetaryTypeDto;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.MonetaryType;
import com.petshop.api.model.mapper.MonetaryTypeMapper;
import com.petshop.api.repository.FinancialPaymentRepository;
import com.petshop.api.repository.MonetaryTypeRepository;
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
class MonetaryTypeServiceTest {

    @InjectMocks
    private MonetaryTypeService monetaryTypeService;

    @Mock
    private MonetaryTypeRepository monetaryTypeRepository;

    @Mock
    private MonetaryTypeMapper monetaryTypeMapper;

    @Mock
    private FinancialPaymentRepository financialPaymentRepository;

    @Mock
    private ValidatorEntities validatorEntities;


    @Test
    @DisplayName("Should return a page of monetary types")
    void getAllMonetaryTypes_ShouldReturnPageOfMonetaryTypes(){

        Pageable pageable = PageRequest.of(0,10);
        MonetaryType monetaryType = new MonetaryType();
        MonetaryTypeResponseDto responseDto = new MonetaryTypeResponseDto();
        Page<MonetaryType> monetaryTypePage = new PageImpl<>(List.of(monetaryType));

        when(monetaryTypeRepository.findAll(pageable)).thenReturn(monetaryTypePage);
        when(monetaryTypeMapper.toResponseDto(monetaryType)).thenReturn(responseDto);

        Page<MonetaryTypeResponseDto> result = monetaryTypeService.getAllMonetaryTypes(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(monetaryTypeRepository).findAll(pageable);

    }


    @Test
    @DisplayName("Should return monetary type when ID exists")
    void getMonetaryTypeById_ShouldReturnMonetaryType_WhenIdExists(){

        UUID id = UUID.randomUUID();
        MonetaryType monetaryType = new MonetaryType();
        monetaryType.setId(id);
        MonetaryTypeResponseDto responseDto = new MonetaryTypeResponseDto();

        when(validatorEntities.validate(id, monetaryTypeRepository, "Monetary type")). thenReturn(monetaryType);
        when(monetaryTypeMapper.toResponseDto(monetaryType)).thenReturn(responseDto);

        MonetaryTypeResponseDto result = monetaryTypeService.getMonetaryTypeById(id);

        assertThat(result).isNotNull();
        verify(validatorEntities).validate(id, monetaryTypeRepository, "Monetary type");

    }


    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID not exists")
    void getMonetaryTypeById_ShouldThrowException_WhenIdDoesNotExists(){

        UUID id = UUID.randomUUID();
        String expectedMessage = "Monetary type not found";

        when(validatorEntities.validate(id, monetaryTypeRepository, "Monetary type")).thenThrow(new ResourceNotFoundException(expectedMessage));

        assertThatThrownBy(() ->monetaryTypeService.getMonetaryTypeById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(expectedMessage);

        verify(validatorEntities).validate(id, monetaryTypeRepository, "Monetary type");
        verifyNoInteractions(monetaryTypeMapper);

    }


    @Test
    @DisplayName("Should return page of monetary types  when searching by name")
    void getMonetaryTypeByName_ShouldReturnPage_WhenNameExistis(){

        String name = "PIX";
        Pageable pageable = PageRequest.of(0,10);
        MonetaryType monetaryType = new MonetaryType();
        MonetaryTypeResponseDto responseDto = new MonetaryTypeResponseDto();
        Page<MonetaryType> monetaryTypePage = new PageImpl<>(List.of(monetaryType));

        when(monetaryTypeRepository.findByNameContainingIgnoreCase(name,pageable)).thenReturn(monetaryTypePage);
        when(monetaryTypeMapper.toResponseDto(monetaryType)).thenReturn(responseDto);

        Page<MonetaryTypeResponseDto> result = monetaryTypeService.getMonetaryTypeByNameContainingIgnoreCase(name, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

    }


    @Test
    @DisplayName("Should create monetary type successfully")
    void createMonetaryType_ShouldReturnDto_WhenSuccessfully(){

        CreateMonetaryType createDto = new CreateMonetaryType();
        MonetaryType monetaryType = new MonetaryType();
        MonetaryType savedMonetaryType = new MonetaryType();
        savedMonetaryType.setId(UUID.randomUUID());
        MonetaryTypeResponseDto responseDto = new MonetaryTypeResponseDto();

        when(monetaryTypeMapper.toEntity(createDto)).thenReturn(monetaryType);
        when(monetaryTypeRepository.save(monetaryType)).thenReturn(savedMonetaryType);
        when(monetaryTypeMapper.toResponseDto(savedMonetaryType)).thenReturn(responseDto);

        MonetaryTypeResponseDto result = monetaryTypeService.createMonetaryType(createDto);

        assertThat(result).isNotNull();
        verify(monetaryTypeRepository).save(monetaryType);

    }


    @Test
    @DisplayName("Should update monetary type successfully")
    void updateMonetaryType_ShouldReturnUpdateDto_WhenSuccessful(){

        UUID id = UUID.randomUUID();
        UpdateMonetaryTypeDto updateDto = new UpdateMonetaryTypeDto();
        MonetaryType monetaryType = new MonetaryType();
        monetaryType.setId(id);
        MonetaryType savedMonetaryType = new MonetaryType();
        MonetaryTypeResponseDto responseDto= new MonetaryTypeResponseDto();

        when(validatorEntities.validate(id, monetaryTypeRepository, "Monetary type")).thenReturn(monetaryType);
        when(monetaryTypeRepository.save(monetaryType)).thenReturn(savedMonetaryType);
        when(monetaryTypeMapper.toResponseDto(savedMonetaryType)).thenReturn(responseDto);

        MonetaryTypeResponseDto result = monetaryTypeService.updateMonetaryType(id,updateDto);

        assertThat(result).isNotNull();
        verify(monetaryTypeMapper).updateMonetaryTypeFromDto(updateDto,monetaryType);
        verify(monetaryTypeRepository).save(monetaryType);

    }


    @Test
    @DisplayName("Should delete monetary type when its not in use")
    void deleteMonetaryType_ShouldDelete_WhenNotInUse(){
        UUID id = UUID.randomUUID();
        MonetaryType monetaryType = new MonetaryType();
        monetaryType.setId(id);

        when(validatorEntities.validate(id, monetaryTypeRepository, "Monetary type")).thenReturn(monetaryType);
        when(financialPaymentRepository.existsByMonetaryType(monetaryType)).thenReturn(false);

        monetaryTypeService.deleteMonetaryType(id);

        verify(monetaryTypeRepository).delete(monetaryType);

    }


    @Test
    @DisplayName("Should throw BusinessException when trying to delete monetary types when its in use")
    void deleteMonetaryType_ShouldThrowException_WhenMonetaryTypeIsInUse(){

        UUID id = UUID.randomUUID();
        MonetaryType monetaryType = new MonetaryType();
        monetaryType.setId(id);

        when(validatorEntities.validate(id,monetaryTypeRepository, "Monetary type")).thenReturn(monetaryType);
        when(financialPaymentRepository.existsByMonetaryType(monetaryType)).thenReturn(true);

        assertThatThrownBy(()->monetaryTypeService.deleteMonetaryType(id))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cannot delete this monetary type because it is being used by financial");

        verify(monetaryTypeRepository, never()).delete(any());
    }
}