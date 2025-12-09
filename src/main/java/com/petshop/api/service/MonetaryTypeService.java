package com.petshop.api.service;

import com.petshop.api.dto.request.CreateMonetaryType;
import com.petshop.api.dto.response.MonetaryTypeResponseDto;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.MonetaryType;
import com.petshop.api.model.mapper.MonetaryTypeMapper;
import com.petshop.api.repository.FinancialRepository;
import com.petshop.api.repository.MonetaryTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MonetaryTypeService {
    private final MonetaryTypeRepository monetaryTypeRepository;
    private final MonetaryTypeMapper monetaryTypeMapper;
    private final FinancialRepository financialRepository;


    public MonetaryTypeResponseDto getMonetaryTypeById(UUID id){
        return monetaryTypeRepository.findById(id)
                .map(monetaryTypeMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Monetary type not found with id: " + id));
    }

    public Page<MonetaryTypeResponseDto> getAllMonetaryTypes(Pageable pageable) {
        return monetaryTypeRepository.findAll(pageable)
                .map(monetaryTypeMapper::toResponseDto);
    }

    public Page<MonetaryTypeResponseDto> getMonetaryTypeByName(String name, Pageable pageable){
        return monetaryTypeRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(monetaryTypeMapper::toResponseDto);
    }

    @Transactional
    public MonetaryTypeResponseDto createMonetaryType(CreateMonetaryType createMonetaryTypeDTO){
        MonetaryType monetaryType = monetaryTypeMapper.toEntity(createMonetaryTypeDTO);
        return monetaryTypeMapper.toResponseDto(monetaryTypeRepository.save(monetaryType));
    }

    @Transactional
    public void deleteMonetaryType(UUID id){
        if (!monetaryTypeRepository.existsById(id)){
            throw new ResourceNotFoundException("Monetary type not found with id: " + id);
             }
        if (financialRepository.existsByMonetaryTypeId(id)){
            throw new BusinessException("Cannot delete this monetary type because it is being used by financial");
             }
        monetaryTypeRepository.deleteById(id);
    }
}
