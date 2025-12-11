package com.petshop.api.domain.financial;


import com.petshop.api.exception.BusinessException;
import com.petshop.api.model.entities.Financial;
import com.petshop.api.model.entities.FinancialPayment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
public class FinancialPaymentGenerator {

    public void addPayment(Financial financial, FinancialPayment payment) {
        payment.setFinancial(financial);
        if(financial.getFinancialPayments() == null) {
            throw  new BusinessException("FinancialPayments is null");
        }
        financial.getFinancialPayments().add(payment);

        if (payment.getMonetaryType() != null) {
            financial.setMonetaryType(payment.getMonetaryType());
        }

        if (financial.getBalance() == null) {
            financial.setBalance(financial.getAmount());
        }

        BigDecimal newBalance = financial.getBalance().subtract(payment.getPaidAmount());

        if (newBalance.compareTo(financial.getBalance()) <=0) {
            financial.setBalance(BigDecimal.ZERO);
            financial.setIsPaid(true);
            financial.setPaymentDate(payment.getPaymentDate());
        }else  {
            financial.setBalance(newBalance);
            financial.setIsPaid(false);
        }
    }
}
