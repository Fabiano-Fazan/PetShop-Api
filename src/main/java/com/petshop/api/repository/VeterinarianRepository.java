package com.petshop.api.repository;

import com.petshop.api.model.entities.Veterinarian;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VeterinarianRepository extends JpaRepository<Veterinarian, UUID> {
    Page<Veterinarian> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
