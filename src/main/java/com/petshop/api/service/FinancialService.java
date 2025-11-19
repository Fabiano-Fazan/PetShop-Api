package com.petshop.api.service;

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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class FinancialService {
    private final FinancialRepository financialRepository;
    private final SaleRepository saleRepository;
    private final ClientRepository clientRepository;
    private final FinancialMapper financialMapper;


    @Transactional
    public FinancialDtoResponse getFinancialById(UUID id) {
        return financialRepository.findById(id)
                .map(financialMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Financial not found with ID: " + id));
    }

    @Transactional
    public Page<FinancialDtoResponse> getAllFinancial(Pageable pageable){
        return financialRepository.findAll(pageable)
                .map(financialMapper::toResponseDto);
    }

    @Transactional
    public Page<FinancialDtoResponse> getByClientName(String name, Pageable pageable) {
        return financialRepository.findByClientName(name, pageable)
                .map(financialMapper::toResponseDto);
    }

    @Transactional
    public void createFinancial(Client client, Sale sale, String description, BigDecimal amount, LocalDate dueDate, LocalDate paymentDate, Boolean isPaid, Integer installment){
        Sale saleDB = saleRepository.findById(sale.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with ID: " + sale.getId()));
        client = clientRepository.findById(sale.getClient().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + sale.getClient().getId()));
        Financial financial = Financial.createFinancial(client,
                saleDB,
                description,
                amount,
                dueDate,
                paymentDate,
                isPaid,
                installment);
        financialRepository.save(financial);
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
