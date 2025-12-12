package com.petshop.api.domain.financial;

import com.petshop.api.model.entities.Financial;
import com.petshop.api.model.entities.FinancialPayment;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;


@Component
public class FinancialPaymentGenerator {

    public void addPayment(Financial financial, FinancialPayment payment) {
        payment.setFinancial(financial);

        if(financial.getFinancialPayments() == null) {
            financial.setFinancialPayments(new ArrayList<>());
        }
        financial.getFinancialPayments().add(payment);

        BigDecimal currentBalance = financial.getBalance() != null ? financial.getBalance() : financial.getAmount();
        BigDecimal newBalance = currentBalance.subtract(payment.getPaidAmount());

        if (newBalance.compareTo(BigDecimal.ZERO) <=0) {
            financial.setIsPaid(true);
            financial.setPaymentDate(payment.getPaymentDate());
            financial.setBalance(BigDecimal.ZERO);
        }else  {
            financial.setBalance(newBalance);
            financial.setIsPaid(false);
        }
    }

    public void revertPayment(Financial financial, FinancialPayment paymentToRevert) {
        BigDecimal currentBalance = financial.getBalance() != null ? financial.getBalance() : BigDecimal.ZERO;
        BigDecimal newBalance = currentBalance.add(paymentToRevert.getPaidAmount());
        financial.setBalance(newBalance);
        financial.setIsPaid(false);
        if (paymentToRevert.getPaymentDate() != null) {
            financial.setPaymentDate(null);
        }
        if(financial.getFinancialPayments() != null) {
            financial.getFinancialPayments().remove(paymentToRevert);
        }
    }
}
