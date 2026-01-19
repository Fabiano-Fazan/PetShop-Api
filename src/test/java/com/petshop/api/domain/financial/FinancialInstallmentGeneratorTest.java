package com.petshop.api.domain.financial;

import com.petshop.api.dto.request.CreateFinancialDto;
import com.petshop.api.model.entities.Client;
import com.petshop.api.model.entities.Financial;
import com.petshop.api.model.entities.Sale;
import com.petshop.api.model.enums.SalePaymentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FinancialInstallmentGeneratorTest {

    @InjectMocks
    private FinancialInstallmentGenerator installmentGenerator;

    @Mock
    private FinancialPaymentGenerator paymentGenerator;


    @Test
    @DisplayName("Should generate single installment for CASH sale")
    void generateInstallmentsFromSale_Cash_ShouldReturnOneInstallmentPaid() {

        LocalDate today = LocalDate.now();
        Client client = new Client();
        Sale sale = new Sale();
        sale.setId(UUID.randomUUID());
        sale.setClient(client);
        sale.setTotalValue(new BigDecimal("100.00"));
        sale.setPaymentType(SalePaymentType.CASH);
        sale.setNotes("Cash Sale");

        List<Financial> result = installmentGenerator.generateInstallmentsFromSale(sale, 1, 0, today);

        assertThat(result).hasSize(1);
        Financial financial = result.getFirst();

        assertThat(financial.getAmount()).isEqualTo(new BigDecimal("100.00"));
        assertThat(financial.getDescription()).isEqualTo("Sale " + sale.getId());
        verify(paymentGenerator).addPayment(any(Financial.class), any());
    }


    @Test
    @DisplayName("Should generate multiple installments for CREDIT sale with correct math (rounding)")
    void generateInstallmentsFromSale_Credit_ShouldHandleRoundingCorrectly() {

        LocalDate today = LocalDate.now();
        Client client = new Client();
        Sale sale = new Sale();
        sale.setId(UUID.randomUUID());
        sale.setClient(client);
        sale.setTotalValue(new BigDecimal("100.00"));
        sale.setPaymentType(SalePaymentType.INSTALLMENTS);

        Integer qty = 3;
        Integer interval = 30;

        List<Financial> result = installmentGenerator.generateInstallmentsFromSale(sale, qty, interval, today);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getAmount()).isEqualTo(new BigDecimal("33.33"));
        assertThat(result.get(1).getAmount()).isEqualTo(new BigDecimal("33.33"));
        assertThat(result.get(2).getAmount()).isEqualTo(new BigDecimal("33.34"));
        assertThat(result.get(0).getDueDate()).isEqualTo(today.plusDays(30));
        assertThat(result.get(1).getDueDate()).isEqualTo(today.plusDays(60));
        assertThat(result.get(2).getDueDate()).isEqualTo(today.plusDays(90));
        assertThat(result.get(0).getDescription()).contains("Installment 1/3");
    }


    @Test
    @DisplayName("Should generate installments from DTO correctly")
    void generateInstallmentsFromDto_ShouldMapFieldsCorrectly() {

        Client client = new Client();
        CreateFinancialDto dto = new CreateFinancialDto();
        dto.setAmount(new BigDecimal("200.00"));
        dto.setDescription("Manual Service");
        dto.setInstallments(2);
        dto.setIntervalDays(15);
        dto.setDueDate(LocalDate.now());
        dto.setIsPaid(false);
        dto.setNotes("Test Note");

        List<Financial> result = installmentGenerator.generateInstallmentsFromDto(client, dto, 2, 15);

        assertThat(result).hasSize(2);

        assertThat(result.getFirst().getAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(result.getFirst().getDescription()).isEqualTo("Manual Service - Installment 1/2");
        assertThat(result.getFirst().getDueDate()).isEqualTo(dto.getDueDate().plusDays(15));
        assertThat(result.getFirst().getClient()).isEqualTo(client);
        assertThat(result.getFirst().getNotes()).isEqualTo("Test Note");
    }
}