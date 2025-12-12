package com.petshop.api.repository;

import com.petshop.api.model.entities.FinancialPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FinancialPaymentRepository extends JpaRepository<FinancialPayment, UUID> {
    boolean existsByMonetaryTypeId(UUID id);
}
