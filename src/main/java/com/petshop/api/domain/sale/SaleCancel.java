package com.petshop.api.domain.sale;

import com.petshop.api.exception.BusinessException;
import com.petshop.api.model.entities.Financial;
import com.petshop.api.model.entities.Sale;
import com.petshop.api.model.enums.SaleStatus;
import org.springframework.stereotype.Component;

@Component
public class SaleCancel {

    public void cancel(Sale sale) {
        if (sale.getStatus() == SaleStatus.CANCELED) {
            throw new BusinessException("This sale is already canceled");
        }

        boolean hasPaidInstallments = sale.getFinancial().stream()
                .anyMatch(Financial::getIsPaid);

        if (hasPaidInstallments) {
            throw new BusinessException("Cannot cancel a sale with paid installments.");
        }

        sale.setStatus(SaleStatus.CANCELED);

        if (sale.getFinancial() != null) {
            sale.getFinancial().clear();
        }
    }
}
