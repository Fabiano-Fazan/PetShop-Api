package com.petshop.api.repository;

import com.petshop.api.model.entities.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {

   Page<ProductCategory> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
