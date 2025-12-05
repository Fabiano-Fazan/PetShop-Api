package com.petshop.api.repository;

import com.petshop.api.model.entities.MonetaryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MonetaryTypeRepository extends JpaRepository<MonetaryType, UUID> {

    Page<MonetaryType> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

