package com.petshop.api.service;

import com.petshop.api.domain.financial.FinancialInstallmentGenerator;
import com.petshop.api.domain.financial.FinancialPaymentGenerator;
import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateFinancialDto;
import com.petshop.api.dto.request.CreateFinancialPaymentDto;
import com.petshop.api.dto.response.FinancialResponseDto;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.Financial;
import com.petshop.api.model.entities.Sale;
import com.petshop.api.model.mapper.FinancialMapper;
import com.petshop.api.repository.ClientRepository;
import com.petshop.api.repository.FinancialPaymentRepository;
import com.petshop.api.repository.FinancialRepository;
import com.petshop.api.repository.MonetaryTypeRepository;
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
    private final FinancialPaymentRepository financialPaymentRepository;
    private final MonetaryTypeRepository monetaryTypeRepository;
    private final ClientRepository clientRepository;
    private final FinancialMapper financialMapper;
    private final FinancialInstallmentGenerator installmentGenerator;
    private final ValidatorEntities validatorEntities;
    private final FinancialPaymentGenerator paymentGenerator;


    public FinancialResponseDto getFinancialById(UUID id) {
        var financial = validatorEntities.validate(id, financialRepository, "Financial");
        return financialMapper.toResponseDto(financial);
    }

    public Page<FinancialResponseDto> getAllFinancial(Pageable pageable){
        return financialRepository.findAll(pageable)
                .map(financialMapper::toResponseDto);
    }

    public Page<FinancialResponseDto> getByClientNameContainingIgnoreCase(String name, Pageable pageable) {
        return financialRepository.findByClientNameContainingIgnoreCase(name, pageable)
                .map(financialMapper::toResponseDto);
    }

    @Transactional
    public void createFinancialFromSale(Sale sale, Integer qtyInstallments, Integer intervalDays){
        LocalDate today = LocalDate.now();
        List<Financial> installments = installmentGenerator.generateInstallmentsFromSale(
                sale,
                qtyInstallments,
                intervalDays,
                today
        );
        financialRepository.saveAll(installments);
    }

    @Transactional
    public List<FinancialResponseDto> createManualFinancial(CreateFinancialDto dto){
        var client = validatorEntities.validate(dto.getClientId(), clientRepository, "Client");
        List<Financial> financials = installmentGenerator.generateInstallmentsFromDto(
                client,
                dto,
                dto.getInstallments(),
                dto.getIntervalDays()
        );
        List<Financial> savedFinancials = financialRepository.saveAll(financials);
        return savedFinancials.stream()
                .map(financialMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public FinancialResponseDto payFinancial(UUID id, CreateFinancialPaymentDto paymentDto){
        var financial = validatorEntities.validate(id,financialRepository, "Financial");
        if(paymentDto.getPaidAmount().compareTo(financial.getBalance()) > 0){
            throw new BusinessException("The paid amount cannot be greater than the financial amount.");
        }
        var financialPayment = financialMapper.toPaymentEntity(paymentDto);
        financialPayment.setMonetaryType(validatorEntities.validate(paymentDto.getMonetaryTypeId(),monetaryTypeRepository, "Monetary Type"));
        paymentGenerator.addPayment(financial, financialPayment);
        if(!financial.getIsPaid()&& paymentDto.getNextDueDate() != null){
            financial.setDueDate(paymentDto.getNextDueDate());
        }
        return financialMapper.toResponseDto(financialRepository.save(financial));
    }

    @Transactional
    public FinancialResponseDto refundFinancial(UUID id){
        var financialPayment = validatorEntities.validate(id, financialPaymentRepository, "Financial Payment");
        var financial = financialPayment.getFinancial();
        paymentGenerator.revertPayment(financial, financialPayment);
        financialPaymentRepository.delete(financialPayment);
        return financialMapper.toResponseDto(financialRepository.save(financial));
    }

    @Transactional
    public void deleteFinancial(UUID id){
        if(!financialRepository.existsById(id)){
            throw new ResourceNotFoundException("Financial not found");
        }
        financialRepository.deleteById(id);
    }

}
