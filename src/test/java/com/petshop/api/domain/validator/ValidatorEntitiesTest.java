package com.petshop.api.domain.validator;

import com.petshop.api.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidatorEntitiesTest {

    @InjectMocks
    private ValidatorEntities validatorEntities;

    @Mock
    private JpaRepository<Object, UUID> repository;

    @Test
    @DisplayName("Should return entity when ID is found in repository")
    void validate_ShouldReturnEntity_WhenIdExists() {

        UUID id = UUID.randomUUID();
        Object mockEntity = new Object();
        String entityName = "Test Entity";

        when(repository.findById(id)).thenReturn(Optional.of(mockEntity));

        Object result = validatorEntities.validate(id, repository, entityName);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(mockEntity);
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID is not found")
    void validate_ShouldThrowException_WhenIdDoesNotExist() {

        UUID id = UUID.randomUUID();
        String entityName = "Custom Entity";

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> validatorEntities.validate(id, repository, entityName))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Custom Entity not found");

        verify(repository).findById(id);
    }
}