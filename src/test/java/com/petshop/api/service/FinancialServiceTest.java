package com.petshop.api.service;

import com.petshop.api.domain.financial.FinancialInstallmentGenerator;
import com.petshop.api.domain.financial.FinancialPaymentGenerator;
import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateFinancialDto;
import com.petshop.api.dto.request.CreateFinancialPaymentDto;
import com.petshop.api.dto.response.FinancialResponseDto;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.model.entities.*;
import com.petshop.api.model.mapper.FinancialMapper;
import com.petshop.api.repository.ClientRepository;
import com.petshop.api.repository.FinancialPaymentRepository;
import com.petshop.api.repository.FinancialRepository;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinancialServiceTest {

    @InjectMocks
    private FinancialService financialService;

    @Mock
    private FinancialRepository financialRepository;

    @Mock
    private FinancialPaymentRepository financialPaymentRepository;

    @Mock
    private MonetaryTypeRepository monetaryTypeRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private FinancialMapper financialMapper;

    @Mock
    private FinancialInstallmentGenerator installmentGenerator;

    @Mock
    private ValidatorEntities validatorEntities;

    @Mock
    private FinancialPaymentGenerator paymentGenerator;


    @Test
    @DisplayName("Should return financial when ID exists")
    void getFinancialById_ShouldReturnFinancial_WhenIdExists() {

        UUID id = UUID.randomUUID();
        Financial financial = new Financial();
        financial.setId(id);
        FinancialResponseDto responseDto = new FinancialResponseDto();

        when(validatorEntities.validate(id, financialRepository, "Financial")).thenReturn(financial);
        when(financialMapper.toResponseDto(financial)).thenReturn(responseDto);

        FinancialResponseDto result = financialService.getFinancialById(id);

        assertThat(result).isNotNull();
        verify(validatorEntities).validate(id, financialRepository, "Financial");
    }


    @Test
    @DisplayName("Should return page of financials")
    void getAllFinancial_ShouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);
        Financial financial = new Financial();
        FinancialResponseDto responseDto = new FinancialResponseDto();
        Page<Financial> page = new PageImpl<>(List.of(financial));

        when(financialRepository.findAll(pageable)).thenReturn(page);
        when(financialMapper.toResponseDto(financial)).thenReturn(responseDto);

        Page<FinancialResponseDto> result = financialService.getAllFinancial(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }


    @Test
    @DisplayName("Should return page of financials filtered by client name")
    void getByClientNameContainingIgnoreCase_ShouldReturnPage() {

        String name = "John";
        Pageable pageable = PageRequest.of(0, 10);
        Financial financial = new Financial();
        FinancialResponseDto responseDto = new FinancialResponseDto();
        Page<Financial> page = new PageImpl<>(List.of(financial));

        when(financialRepository.findByClientNameContainingIgnoreCase(name, pageable)).thenReturn(page);
        when(financialMapper.toResponseDto(financial)).thenReturn(responseDto);

        Page<FinancialResponseDto> result = financialService.getByClientNameContainingIgnoreCase(name, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }


    @Test
    @DisplayName("Should create financials from sale successfully")
    void createFinancialFromSale_ShouldSaveInstallments() {

        Sale sale = new Sale();
        Integer qtyInstallments = 3;
        Integer interval = 30;
        Financial financial = new Financial();
        List<Financial> installments = List.of(financial);

        when(installmentGenerator.generateInstallmentsFromSale(eq(sale), eq(qtyInstallments), eq(interval), any(LocalDate.class))).thenReturn(installments);

        financialService.createFinancialFromSale(sale, qtyInstallments, interval);

        verify(installmentGenerator).generateInstallmentsFromSale(eq(sale), eq(qtyInstallments), eq(interval), any(LocalDate.class));
        verify(financialRepository).saveAll(installments);
    }



    @Test
    @DisplayName("Should create manual financials successfully")
    void createManualFinancial_ShouldReturnListDto() {

        CreateFinancialDto createDto = new CreateFinancialDto();
        createDto.setClientId(UUID.randomUUID());
        createDto.setInstallments(2);
        createDto.setIntervalDays(15);
        Client client = new Client();
        Financial financial = new Financial();
        List<Financial> financials = List.of(financial);
        FinancialResponseDto responseDto = new FinancialResponseDto();

        when(validatorEntities.validate(createDto.getClientId(), clientRepository, "Client")).thenReturn(client);
        when(installmentGenerator.generateInstallmentsFromDto(client, createDto, createDto.getInstallments(), createDto.getIntervalDays())).thenReturn(financials);
        when(financialRepository.saveAll(financials)).thenReturn(financials);
        when(financialMapper.toResponseDto(financial)).thenReturn(responseDto);

        List<FinancialResponseDto> result = financialService.createManualFinancial(createDto);

        assertThat(result).hasSize(1);
        verify(financialRepository).saveAll(financials);
    }


    @Test
    @DisplayName("Should pay financial successfully")
    void payFinancial_ShouldReturnDto_WhenSuccessful() {
        UUID id = UUID.randomUUID();
        CreateFinancialPaymentDto paymentDto = new CreateFinancialPaymentDto();
        paymentDto.setPaidAmount(new BigDecimal("50.00"));
        paymentDto.setMonetaryTypeId(UUID.randomUUID());
        paymentDto.setNextDueDate(LocalDate.now().plusDays(30));
        Financial financial = new Financial();
        financial.setBalance(new BigDecimal("100.00"));
        financial.setIsPaid(false);
        FinancialPayment paymentEntity = new FinancialPayment();
        MonetaryType monetaryType = new MonetaryType();

        when(validatorEntities.validate(id, financialRepository, "Financial")).thenReturn(financial);
        when(financialMapper.toPaymentEntity(paymentDto)).thenReturn(paymentEntity);
        when(validatorEntities.validate(paymentDto.getMonetaryTypeId(), monetaryTypeRepository, "Monetary Type")).thenReturn(monetaryType);
        when(financialRepository.save(financial)).thenReturn(financial);
        when(financialMapper.toResponseDto(financial)).thenReturn(new FinancialResponseDto());

        FinancialResponseDto result = financialService.payFinancial(id, paymentDto);

        assertThat(result).isNotNull();
        verify(paymentGenerator).addPayment(financial, paymentEntity);
        assertThat(financial.getDueDate()).isEqualTo(paymentDto.getNextDueDate());
        verify(financialRepository).save(financial);
    }

    @Test
    @DisplayName("Should throw BusinessException when paid amount is greater than balance")
    void payFinancial_ShouldThrowException_WhenAmountIsGreater() {

        UUID id = UUID.randomUUID();
        CreateFinancialPaymentDto paymentDto = new CreateFinancialPaymentDto();
        paymentDto.setPaidAmount(new BigDecimal("150.00"));
        Financial financial = new Financial();
        financial.setBalance(new BigDecimal("100.00"));

        when(validatorEntities.validate(id, financialRepository, "Financial")).thenReturn(financial);

        assertThatThrownBy(() -> financialService.payFinancial(id, paymentDto))
                .isInstanceOf(BusinessException.class)
                .hasMessage("The paid amount cannot be greater than the financial amount.");

        verify(paymentGenerator, never()).addPayment(any(), any());
        verify(financialRepository, never()).save(any());
    }


    @Test
    @DisplayName("Should refund financial successfully")
    void refundFinancial_ShouldDeletePaymentAndSaveFinancial() {

        UUID paymentId = UUID.randomUUID();
        FinancialPayment payment = new FinancialPayment();
        Financial financial = new Financial();
        payment.setFinancial(financial);

        when(validatorEntities.validate(paymentId, financialPaymentRepository, "Financial Payment"))
                .thenReturn(payment);
        when(financialRepository.save(financial)).thenReturn(financial);
        when(financialMapper.toResponseDto(financial)).thenReturn(new FinancialResponseDto());

        FinancialResponseDto result = financialService.refundFinancial(paymentId);

        assertThat(result).isNotNull();
        verify(paymentGenerator).revertPayment(financial, payment);
        verify(financialPaymentRepository).delete(payment);
        verify(financialRepository).save(financial);
    }


    @Test
    @DisplayName("Should delete financial when not paid")
    void deleteFinancial_ShouldDelete_WhenNotPaid() {
        UUID id = UUID.randomUUID();
        Financial financial = new Financial();
        financial.setId(id);
        financial.setIsPaid(false);

        when(validatorEntities.validate(id, financialRepository, "Financial")).thenReturn(financial);

        financialService.deleteFinancial(id);

        verify(financialRepository).delete(financial);
    }

    @Test
    @DisplayName("Should throw BusinessException when trying to delete paid financial")
    void deleteFinancial_ShouldThrowException_WhenPaid() {
        UUID id = UUID.randomUUID();
        Financial financial = new Financial();
        financial.setId(id);
        financial.setIsPaid(true);

        when(validatorEntities.validate(id, financialRepository, "Financial")).thenReturn(financial);

        assertThatThrownBy(() -> financialService.deleteFinancial(id))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cannot delete financial that has been paid");

        verify(financialRepository, never()).delete(any());
    }
}