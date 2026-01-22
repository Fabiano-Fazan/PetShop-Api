package com.petshop.api.service;

import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateAnimalDto;
import com.petshop.api.dto.response.AnimalResponseDto;
import com.petshop.api.dto.update.UpdateAnimalDto;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.Animal;
import com.petshop.api.model.mapper.AnimalMapper;
import com.petshop.api.repository.AnimalRepository;
import com.petshop.api.repository.MedicalAppointmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @InjectMocks
    private AnimalService animalService;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private MedicalAppointmentRepository medicalAppointmentRepository;

    @Mock
    private AnimalMapper animalMapper;

    @Mock
    private ValidatorEntities validatorEntities;


    @Test
    @DisplayName("Should return a page of animals")
    void getAllAnimals_ShouldReturnPageOfAnimals(){

        Pageable pageable = PageRequest.of(0,10);
        Animal animal = new Animal();
        AnimalResponseDto responseDto = new AnimalResponseDto();
        Page<Animal> animalsPage = new PageImpl<>(List.of(animal));

        when(animalRepository.findAll(pageable)). thenReturn(animalsPage);
        when(animalMapper.toResponseDto(animal)). thenReturn(responseDto);

        Page<AnimalResponseDto> result = animalService.getAllAnimals(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

        verify(animalRepository).findAll(pageable);

    }


    @Test
    @DisplayName("Should return animal when ID exists")
    void getAnimalById_ShouldReturnAnimal_WhenIdExists(){

        UUID id = UUID.randomUUID();
        Animal animal= new Animal();
        animal.setId(id);
        AnimalResponseDto responseDto= new AnimalResponseDto();

        when(validatorEntities.validate(id, animalRepository, "Animal")).thenReturn(animal);
        when(animalMapper.toResponseDto(animal)).thenReturn(responseDto);

        AnimalResponseDto result = animalService.getAnimalById(id);

        assertThat(result).isNotNull();

        verify(validatorEntities).validate(id,animalRepository, "Animal");

    }


    @Test
    @DisplayName("Should throw ResourceNorFoundException When ID not exists")
    void getAnimalById_ShouldThrowException_WhenIdDoesNotExist(){

        UUID id = UUID.randomUUID();
        String expectedMessage = "Animal not found";

        when(validatorEntities.validate(id, animalRepository, "Animal"))
                .thenThrow(new ResourceNotFoundException(expectedMessage));

        assertThatThrownBy(() -> animalService.getAnimalById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(expectedMessage);

        verify(validatorEntities).validate(id,animalRepository,"Animal");
        verifyNoInteractions(animalMapper);

    }


    @Test
    @DisplayName("Should return page of animals when searching by name")
    void getAnimalByName_ShouldReturnPage_WhenNameExists(){

        String name = "Maya";
        Pageable pageable = PageRequest.of(0,10);
        Animal animal = new Animal();
        AnimalResponseDto responseDto = new AnimalResponseDto();
        Page<Animal> animalPage = new PageImpl<>(List.of(animal));

        when(animalRepository.findByNameContainingIgnoreCase(name, pageable)).thenReturn(animalPage);
        when(animalMapper.toResponseDto(animal)).thenReturn(responseDto);

        Page<AnimalResponseDto> result = animalService.getAnimalByNameContainingIgnoreCase(name, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

    }

    @Test
    @DisplayName("Should return page of animals when searching by species")
    void getAnimalBySpecies_ShouldReturnPage_WhenSpeciesExists(){

        String species = "vira-lata";
        Pageable pageable = PageRequest.of(0,10);
        Animal animal = new Animal();
        AnimalResponseDto responseDto = new AnimalResponseDto();
        Page<Animal> animalPage = new PageImpl<>(List.of(animal));

        when(animalRepository.findBySpeciesContainingIgnoreCase(species, pageable)).thenReturn(animalPage);
        when(animalMapper.toResponseDto(animal)).thenReturn(responseDto);

        Page<AnimalResponseDto> result = animalService.getAnimalsBySpecies(species, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);

    }


    @Test
    @DisplayName("Should create animal successfully")
    void createAnimal_ShouldReturnDto_WhenSuccessful(){

        CreateAnimalDto createDto = new CreateAnimalDto();
        Animal animal = new Animal();
        Animal savedAnimal = new Animal();
        savedAnimal.setId(UUID.randomUUID());
        AnimalResponseDto responseDto = new AnimalResponseDto();

        when(animalMapper.toEntity(createDto)).thenReturn(animal);
        when(animalRepository.save(animal)).thenReturn(savedAnimal);
        when(animalMapper.toResponseDto(savedAnimal)).thenReturn(responseDto);

        AnimalResponseDto result = animalService.createAnimal(createDto);

        assertThat(result).isNotNull();
        verify(animalRepository).save(animal);

    }


    @Test
    @DisplayName("Should update animal successfully")
    void updateAnimal_ShouldReturnUpdatedDto_WhenSuccessful(){

        UUID id = UUID.randomUUID();
        UpdateAnimalDto updateDto = new UpdateAnimalDto();
        Animal animal = new Animal();
        animal.setId(id);
        Animal savedAnimal = new Animal();
        AnimalResponseDto responseDto = new AnimalResponseDto();

        when(validatorEntities.validate(id, animalRepository, "Animal")).thenReturn(animal);
        when(animalRepository.save(animal)).thenReturn(savedAnimal);
        when(animalMapper.toResponseDto(savedAnimal)).thenReturn(responseDto);

        AnimalResponseDto result = animalService.updateAnimal(id,updateDto);

        assertThat(result).isNotNull();
        verify(animalMapper).updateAnimalFromDto(updateDto,animal);
        verify(animalRepository).save(animal);

    }


    @Test
    @DisplayName("Should delete animal when medical appointments not exists")
    void deleteAnimal_ShouldDelete_WhenNoAppointments(){

        UUID id = UUID.randomUUID();
        Animal animal = new Animal();
        animal.setId(id);

        when(validatorEntities.validate(id, animalRepository, "Animal")).thenReturn(animal);
        when(medicalAppointmentRepository.existsByAnimal(animal)).thenReturn(false);

        animalService.deleteAnimal(id);

        verify(animalRepository).delete(animal);

    }


    @Test
    @DisplayName("Should throw BusinessException when trying to delete animal with appointments")
    void deleteAnimal_ShouldThrowException_WhenAnimalHasAppointments(){

        UUID id = UUID.randomUUID();
        Animal animal = new Animal();
        animal.setId(id);

        when(validatorEntities.validate(id,animalRepository,"Animal")).thenReturn(animal);
        when(medicalAppointmentRepository.existsByAnimal(animal)).thenReturn(true);

        assertThatThrownBy(() -> animalService.deleteAnimal(id))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cannot delete this animal because it is being used by medical appointments");

        verify(animalRepository, never()).delete(any());
    }
}