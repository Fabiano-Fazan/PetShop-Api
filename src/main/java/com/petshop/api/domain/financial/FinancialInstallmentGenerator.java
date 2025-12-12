package com.petshop.api.domain.financial;

import com.petshop.api.dto.request.CreateFinancialDto;
import com.petshop.api.model.entities.Client;
import com.petshop.api.model.entities.Financial;
import com.petshop.api.model.entities.FinancialPayment;
import com.petshop.api.model.entities.Sale;
import com.petshop.api.model.enums.SalePaymentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class FinancialInstallmentGenerator {

    private final FinancialPaymentGenerator paymentGenerator;

    public List<Financial> generateInstallmentsFromSale(
            Sale sale,
            Integer qtyInstallments,
            Integer intervalDays,
            LocalDate today

    ){

        if (sale.getPaymentType() == SalePaymentType.CASH) {
            return List.of(buildFinancial(
                    sale.getClient(),
                    sale,
                    "Sale %s".formatted(sale.getId()),
                    1, 1, 0,
                    sale.getTotalValue(), BigDecimal.ZERO,
                    today, true,
                    sale.getNotes()
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
                    false,
                    sale.getNotes()

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
                dto.getIsPaid() != null ? dto.getIsPaid() : false,
                dto.getNotes()
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
            Boolean isPaid,
            String notes
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
                        isPaid,
                        notes
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
            Boolean isPaid,
            String notes
    ) {

        String finalDescription = totalInstallments > 1
                ? "%s - Installment %d/%d".formatted(descriptionBase, installmentNumber, totalInstallments)
                : descriptionBase;

        BigDecimal finalAmount = installmentNumber.equals(totalInstallments)
                ? installmentValue.add(reminder)
                : installmentValue;

        if (startDate == null) {
            startDate = LocalDate.now();
        }
        LocalDate dueDate = startDate.plusDays((long) intervalDays * installmentNumber);

        Financial financial = Financial.builder()
                .client(client)
                .sale(sale)
                .description(finalDescription)
                .amount(finalAmount)
                .balance(finalAmount)
                .dueDate(dueDate)
                .isPaid(false)
                .paymentDate(null)
                .installment(installmentNumber)
                .notes(notes)
                .financialPayments(new ArrayList<>())
                .build();

        if (Boolean.TRUE.equals(isPaid)) {
            FinancialPayment payment = FinancialPayment.builder()
                    .paidAmount(finalAmount)
                    .paymentDate(LocalDate.now())
                    .notes("Paid with cash on creation")
                    .build();
            paymentGenerator.addPayment(financial, payment);
        }
        return financial;
    }
}