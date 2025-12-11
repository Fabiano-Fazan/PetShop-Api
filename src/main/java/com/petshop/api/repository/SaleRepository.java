package com.petshop.api.repository;

import com.petshop.api.model.entities.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SaleRepository extends JpaRepository<Sale, UUID> {

    Page<Sale> findByClientNameContainingIgnoreCase(String name, Pageable pageable);

}
