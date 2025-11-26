package com.petshop.api.repository;

import com.petshop.api.model.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    Page<Client> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    boolean existsByCpf(String cpf);
}
