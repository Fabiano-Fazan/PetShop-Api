package com.petshop.api.repository;

import com.petshop.api.model.entities.Financial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.UUID;

public interface FinancialRepository extends JpaRepository<Financial, UUID> {

    Page<Financial> findByClientByNameContainingIgnoreCase(String name, Pageable pageable);

}
