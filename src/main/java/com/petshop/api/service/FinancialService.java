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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinancialService {
    private final FinancialRepository financialRepository;
    private final SaleRepository saleRepository;
    private final ClientRepository clientRepository;
    private final FinancialMapper financialMapper;

    @Transactional
    public void createFinancial(Client client, Sale sale, String description, BigDecimal amount, LocalDate dueDate, LocalDate paymentDate, Boolean isPaid, Integer installment){
        Sale saleDB = saleRepository.findById(sale.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with ID: " + sale.getId()));
        Client clientDb = clientRepository.findById(sale.getClient().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + sale.getClient().getId()));
        Financial financial = Financial.createFinancial(clientDb, saleDB, description, amount, dueDate, paymentDate, isPaid, installment);
        financialRepository.save(financial);
    }

    @Transactional
    public void createFinancial (CreateFinancialDto createFinancialDTO){
        Sale sale = saleRepository.findById(createFinancialDTO.getSaleId())
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with ID: " + createFinancialDTO.getSaleId()));
        Client client = clientRepository.findById(sale.getClient().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + sale.getClient().getId()));
        this.createFinancial(
                client,
                sale,
                createFinancialDTO.getDescription(),
                createFinancialDTO.getAmount(),
                createFinancialDTO.getDueDate(),
                createFinancialDTO.getPaymentDate(),
                createFinancialDTO.getIsPaid(),
                createFinancialDTO.getInstallment());
    }

    @Transactional
    public FinancialDtoResponse getFinancialById(UUID id) {
        return financialRepository.findById(id)
                .map(financialMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Financial not found with ID: " + id));
    }


}
