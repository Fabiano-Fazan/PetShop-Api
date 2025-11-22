package com.petshop.api.domain;

import com.petshop.api.model.entities.Financial;
import com.petshop.api.model.entities.Sale;
import com.petshop.api.model.enums.PaymentType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class FinancialInstallmentGenerator {


    public List<Financial> generateInstallments(
            Sale sale,
            Integer qtyInstallments,
            Integer intervalDays,
            LocalDate today
    ){
        return sale.getPaymentType() == PaymentType.CASH
                ? generateCashPayment(sale, today)
                : generateCreditPayment(sale, qtyInstallments, intervalDays, today);
    }


    private List<Financial> generateCashPayment(Sale sale, LocalDate today) {
        return List.of(
                buildFinancial(
                        sale,
                        1,
                        1,
                        0,
                        sale.getTotalValue(),
                        BigDecimal.ZERO,
                        today,
                        true
                )
        );
    }
    private List<Financial> generateCreditPayment(
            Sale sale,
            Integer qtyInstallments,
            Integer intervalDays,
            LocalDate today
    ) {
        BigDecimal total = sale.getTotalValue();
        BigDecimal installments = BigDecimal.valueOf(qtyInstallments);
        BigDecimal installmentValue = total.divide(installments, 2, RoundingMode.FLOOR);
        BigDecimal reminder = total.subtract(installmentValue.multiply(installments));

        return IntStream.rangeClosed(1,qtyInstallments)
                .mapToObj(i ->buildFinancial(
                        sale,
                        i,
                        qtyInstallments,
                        intervalDays,
                        installmentValue,
                        reminder,
                        today,
                        false
                ))
                .toList();
    }

    private Financial buildFinancial(
            Sale sale,
            Integer installment,
            Integer qtyInstallments,
            Integer intervalDays,
            BigDecimal installmentValue,
            BigDecimal reminder,
            LocalDate today,
            Boolean isPaid
    ){
        return Financial.builder()
                .client(sale.getClient())
                .sale(sale)
                .description( "Sale %s - Installment %d/%d".formatted(sale.getId(), installment, qtyInstallments))
                .amount(installment.equals(qtyInstallments) ? installmentValue.add(reminder) : installmentValue)
                .dueDate(today.plusDays((long) intervalDays * installment))
                .isPaid(isPaid)
                .paymentDate(isPaid ? today : null)
                .installment(installment)
                .build();
    }
}
