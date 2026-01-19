package com.petshop.api.domain.financial;

import com.petshop.api.model.entities.Financial;
import com.petshop.api.model.entities.FinancialPayment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FinancialPaymentGeneratorTest {

    @InjectMocks
    private FinancialPaymentGenerator paymentGenerator;


    @Test
    @DisplayName("Should add partial payment and update balance correctly")
    void addPayment_PartialPayment_ShouldUpdateBalanceAndNotMarkAsPaid() {

        BigDecimal totalAmount = new BigDecimal("100.00");
        BigDecimal paidAmount = new BigDecimal("40.00");
        BigDecimal expectedBalance = new BigDecimal("60.00");
        Financial financial = new Financial();
        financial.setAmount(totalAmount);
        financial.setBalance(totalAmount);
        financial.setIsPaid(false);
        financial.setFinancialPayments(new ArrayList<>());

        FinancialPayment payment = FinancialPayment.builder()
                .paidAmount(paidAmount)
                .paymentDate(LocalDate.now())
                .build();

        paymentGenerator.addPayment(financial, payment);

        assertThat(financial.getBalance()).isEqualTo(expectedBalance);
        assertThat(financial.getIsPaid()).isFalse();
        assertThat(financial.getFinancialPayments()).contains(payment);
        assertThat(payment.getFinancial()).isEqualTo(financial);
    }


    @Test
    @DisplayName("Should add full payment and mark as paid")
    void addPayment_FullPayment_ShouldMarkAsPaidAndZeroBalance() {

        BigDecimal totalAmount = new BigDecimal("100.00");
        BigDecimal paidAmount = new BigDecimal("100.00");
        Financial financial = new Financial();
        financial.setAmount(totalAmount);
        financial.setBalance(totalAmount);
        financial.setIsPaid(false);

        FinancialPayment payment = FinancialPayment.builder()
                .paidAmount(paidAmount)
                .paymentDate(LocalDate.now())
                .build();

        paymentGenerator.addPayment(financial, payment);

        assertThat(financial.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(financial.getIsPaid()).isTrue();
        assertThat(financial.getPaymentDate()).isEqualTo(payment.getPaymentDate());
    }


    @Test
    @DisplayName("Should revert payment and restore balance")
    void revertPayment_ShouldIncreaseBalanceAndUnmarkPaid() {

        BigDecimal initialAmount = new BigDecimal("100.00");
        BigDecimal paymentAmount = new BigDecimal("100.00");
        Financial financial = new Financial();
        financial.setAmount(initialAmount);
        financial.setBalance(BigDecimal.ZERO);
        financial.setIsPaid(true);
        financial.setPaymentDate(LocalDate.now());
        financial.setFinancialPayments(new ArrayList<>());

        FinancialPayment paymentToRemove = FinancialPayment.builder()
                .paidAmount(paymentAmount)
                .paymentDate(LocalDate.now())
                .build();

        financial.getFinancialPayments().add(paymentToRemove);

        paymentGenerator.revertPayment(financial, paymentToRemove);

        assertThat(financial.getBalance()).isEqualTo(paymentAmount);
        assertThat(financial.getIsPaid()).isFalse();
        assertThat(financial.getPaymentDate()).isNull();
        assertThat(financial.getFinancialPayments()).doesNotContain(paymentToRemove);
    }
}