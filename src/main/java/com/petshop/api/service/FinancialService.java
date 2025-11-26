package com.petshop.api.service;

import com.petshop.api.domain.Financial.FinancialInstallmentGenerator;
import com.petshop.api.domain.Validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateFinancialDto;
import com.petshop.api.dto.response.FinancialResponseDto;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.Financial;
import com.petshop.api.model.entities.Sale;
import com.petshop.api.model.mapper.FinancialMapper;
import com.petshop.api.repository.FinancialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class FinancialService {
    private final FinancialRepository financialRepository;
    private final FinancialMapper financialMapper;
    private final FinancialInstallmentGenerator installmentGenerator;
    private final ValidatorEntities validatorEntities;


    public FinancialResponseDto getFinancialById(UUID id) {
        return financialRepository.findById(id)
                .map(financialMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Financial not found with ID: " + id));
    }

    public Page<FinancialResponseDto> getAllFinancial(Pageable pageable){
        return financialRepository.findAll(pageable)
                .map(financialMapper::toResponseDto);
    }

    public Page<FinancialResponseDto> getByClientNameContainingIgnoreCase(String name, Pageable pageable) {
        return financialRepository.findByClientByNameContainingIgnoreCase(name, pageable)
                .map(financialMapper::toResponseDto);
    }

    @Transactional
    public void createFinancialFromSale(Sale sale, Integer qtyInstallments, Integer intervalDays){
        LocalDate today = LocalDate.now();
        List<Financial> installments = installmentGenerator.generateInstallments(
                sale,
                qtyInstallments,
                intervalDays,
                today
        );
        financialRepository.saveAll(installments);
    }

    @Transactional
    public FinancialResponseDto createManualFinancial(CreateFinancialDto createFinancialDTO){
        Financial financial = financialMapper.toEntity(createFinancialDTO);
        financial.setSale(validatorEntities.validateSale(createFinancialDTO.getSaleId()));
        financial.setClient(validatorEntities.validateClient(createFinancialDTO.getClientId()));
        return financialMapper.toResponseDto(financialRepository.save(financial));
    }

    @Transactional
    public void deleteFinancial(UUID id){
        if(!financialRepository.existsById(id)){
            throw new ResourceNotFoundException("Financial not found with ID: " + id);
        }
        financialRepository.deleteById(id);
    }

}
