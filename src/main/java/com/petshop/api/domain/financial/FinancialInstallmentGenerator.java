package com.petshop.api.domain.financial;

import com.petshop.api.dto.request.CreateFinancialDto;
import com.petshop.api.model.entities.Client;
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

    public List<Financial> generateInstallmentsFromSale(
            Sale sale,
            Integer qtyInstallments,
            Integer intervalDays,
            LocalDate today
    ){

        if (sale.getPaymentType() == PaymentType.CASH) {
            return List.of(buildFinancial(
                    sale.getClient(),
                    sale,
                    "Sale %s".formatted(sale.getId()),
                    1, 1, 0,
                    sale.getTotalValue(), BigDecimal.ZERO,
                    today, true
            ));
        } else {
            return generateGenericInstallments(
                    sale.getClient(),
                    sale,
                    "Sale %s".formatted(sale.getId()),
                    sale.getTotalValue(),
                    qtyInstallments,
                    intervalDays,
                    today,
                    false
            );
        }
    }

    public List<Financial> generateInstallmentsFromDto(
            Client client,
            CreateFinancialDto dto,
            Integer qtyInstallments,
            Integer intervalDays
    ){
        return generateGenericInstallments(
                client,
                null,
                dto.getDescription(),
                dto.getAmount(),
                qtyInstallments,
                intervalDays,
                dto.getDueDate(),
                dto.getIsPaid() != null ? dto.getIsPaid() : false
        );
    }

    private List<Financial> generateGenericInstallments(
            Client client,
            Sale sale,
            String descriptionBase,
            BigDecimal totalValue,
            Integer qtyInstallments,
            Integer intervalDays,
            LocalDate startDate,
            Boolean isPaid
    ) {
        BigDecimal installments = BigDecimal.valueOf(qtyInstallments);
        BigDecimal installmentValue = totalValue.divide(installments, 2, RoundingMode.FLOOR);
        BigDecimal reminder = totalValue.subtract(installmentValue.multiply(installments));

        return IntStream.rangeClosed(1, qtyInstallments)
                .mapToObj(i -> buildFinancial(
                        client,
                        sale,
                        descriptionBase,
                        i,
                        qtyInstallments,
                        intervalDays,
                        installmentValue,
                        reminder,
                        startDate,
                        isPaid
                ))
                .toList();
    }

    private Financial buildFinancial(
            Client client,
            Sale sale,
            String descriptionBase,
            Integer installmentNumber,
            Integer totalInstallments,
            Integer intervalDays,
            BigDecimal installmentValue,
            BigDecimal reminder,
            LocalDate startDate,
            Boolean isPaid
    ){

        String finalDescription = totalInstallments > 1
                ? "%s - Installment %d/%d".formatted(descriptionBase, installmentNumber, totalInstallments)
                : descriptionBase;

        BigDecimal finalAmount = installmentNumber.equals(totalInstallments)
                ? installmentValue.add(reminder)
                : installmentValue;

        LocalDate dueDate = startDate.plusDays((long) intervalDays * installmentNumber);

        return Financial.builder()
                .client(client)
                .sale(sale)
                .description(finalDescription)
                .amount(finalAmount)
                .dueDate(dueDate)
                .isPaid(isPaid)
                .paymentDate(isPaid ? startDate : null)
                .installment(installmentNumber)
                .build();
    }
}