package com.petshop.api.repository;

import com.petshop.api.model.entities.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AnimalRepository extends JpaRepository<Animal, UUID> {
    Page<Animal> findBySpeciesContainingIgnoreCase(String species, Pageable pageable);
    Page<Animal> findAllByNameContainingIgnoreCase(String name, Pageable pageable);



}
