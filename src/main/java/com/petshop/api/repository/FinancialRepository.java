package com.petshop.api.repository;

import com.petshop.api.model.entities.Financial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FinancialRepository extends JpaRepository<Financial, UUID> {
}
