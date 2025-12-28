package com.petshop.api.domain.validator;

import com.petshop.api.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ValidatorEntities {

    public <T> T validate(UUID id, JpaRepository<T, UUID> repository, String entityName) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName + " not found"));
    }
}
