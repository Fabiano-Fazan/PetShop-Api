package com.petshop.api.repository;

import com.petshop.api.model.entities.ProductSale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductSaleRepository extends JpaRepository<ProductSale, UUID> {
    boolean existsByProductId(UUID productId);
}
