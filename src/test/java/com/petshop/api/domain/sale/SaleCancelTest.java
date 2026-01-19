package com.petshop.api.domain.sale;

import com.petshop.api.exception.BusinessException;
import com.petshop.api.model.entities.Financial;
import com.petshop.api.model.entities.Sale;
import com.petshop.api.model.enums.SaleStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class SaleCancelTest {

    @InjectMocks
    private SaleCancel saleCancel;

    @Test
    @DisplayName("Should cancel sale successfully when no installments are paid")
    void cancel_ShouldCancel_WhenNoInstallmentsPaid() {

        Sale sale = new Sale();
        sale.setStatus(SaleStatus.COMPLETED);
        Financial financial1 = new Financial();
        financial1.setIsPaid(false);
        Financial financial2 = new Financial();
        financial2.setIsPaid(false);

        sale.setFinancial(new ArrayList<>(List.of(financial1, financial2)));

        saleCancel.cancel(sale);

        assertThat(sale.getStatus()).isEqualTo(SaleStatus.CANCELED);
        assertThat(sale.getFinancial()).isEmpty();
    }


    @Test
    @DisplayName("Should throw exception if sale is already canceled")
    void cancel_ShouldThrowException_WhenAlreadyCanceled() {

        Sale sale = new Sale();
        sale.setStatus(SaleStatus.CANCELED);

        assertThatThrownBy(() -> saleCancel.cancel(sale))
                .isInstanceOf(BusinessException.class)
                .hasMessage("This sale is already canceled");
    }


    @Test
    @DisplayName("Should throw exception if sale has any paid installment")
    void cancel_ShouldThrowException_WhenHasPaidInstallments() {

        Sale sale = new Sale();
        sale.setStatus(SaleStatus.COMPLETED);
        Financial financial1 = new Financial();
        financial1.setIsPaid(false);
        Financial financial2 = new Financial();
        financial2.setIsPaid(true);

        sale.setFinancial(new ArrayList<>(List.of(financial1, financial2)));

        assertThatThrownBy(() -> saleCancel.cancel(sale))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cannot cancel a sale with paid installments.");


        assertThat(sale.getStatus()).isEqualTo(SaleStatus.COMPLETED);

        assertThat(sale.getFinancial()).hasSize(2);
    }


    @Test
    @DisplayName("Should cancel successfully even if financial list is empty")
    void cancel_ShouldCancel_WhenFinancialListIsEmpty() {

        Sale sale = new Sale();
        sale.setStatus(SaleStatus.COMPLETED);
        sale.setFinancial(new ArrayList<>());
        saleCancel.cancel(sale);

        assertThat(sale.getStatus()).isEqualTo(SaleStatus.CANCELED);
        assertThat(sale.getFinancial()).isEmpty();
    }
}