package com.petshop.api.service;

import com.petshop.api.domain.FinancialInstallmentGenerator;
import com.petshop.api.dto.request.CreateFinancialDto;
import com.petshop.api.dto.response.FinancialDtoResponse;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.Client;
import com.petshop.api.model.entities.Financial;
import com.petshop.api.model.entities.Sale;
import com.petshop.api.model.mapper.FinancialMapper;
import com.petshop.api.repository.ClientRepository;
import com.petshop.api.repository.FinancialRepository;
import com.petshop.api.repository.SaleRepository;
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
    private final SaleRepository saleRepository;
    private final ClientRepository clientRepository;
    private final FinancialMapper financialMapper;
    private final FinancialInstallmentGenerator installmentGenerator;


    public FinancialDtoResponse getFinancialById(UUID id) {
        return financialRepository.findById(id)
                .map(financialMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Financial not found with ID: " + id));
    }

    public Page<FinancialDtoResponse> getAllFinancial(Pageable pageable){
        return financialRepository.findAll(pageable)
                .map(financialMapper::toResponseDto);
    }

    public Page<FinancialDtoResponse> getByClientName(String name, Pageable pageable) {
        return financialRepository.findByClientName(name, pageable)
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
    public FinancialDtoResponse createManualFinancial(CreateFinancialDto createFinancialDTO){
        Sale sale = saleRepository.findById(createFinancialDTO.getSaleId())
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with ID: " + createFinancialDTO.getSaleId()));
        Client client = clientRepository.findById(sale.getClient().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + sale.getClient().getId()));
        Financial financial = financialMapper.toEntity(createFinancialDTO);
        financial.setClient(client);
        financial.setSale(sale);
        financialRepository.save(financial);
        return financialMapper.toResponseDto(financial);
    }

    @Transactional
    public void deleteFinancial(UUID id){
        if(!financialRepository.existsById(id)){
            throw new ResourceNotFoundException("Financial not found with ID: " + id);
        }
        financialRepository.deleteById(id);
    }

}
