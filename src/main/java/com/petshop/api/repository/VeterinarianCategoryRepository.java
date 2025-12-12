package com.petshop.api.repository;

import com.petshop.api.model.entities.VeterinarianCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface VeterinarianCategoryRepository extends JpaRepository<VeterinarianCategory, UUID> {
    Page<VeterinarianCategory> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
