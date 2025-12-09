package com.petshop.api.service;

import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.UpdateAnimalDto;
import com.petshop.api.dto.response.AnimalResponseDto;
import com.petshop.api.dto.request.CreateAnimalDto;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.Animal;
import com.petshop.api.model.mapper.AnimalMapper;
import com.petshop.api.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;
    private final ValidatorEntities validatorEntities;


    public Page<AnimalResponseDto> getAllAnimals(Pageable pageable){
        return animalRepository.findAll(pageable)
                .map(animalMapper::toResponseDto);
    }

    public AnimalResponseDto getAnimalById(UUID id){
        return animalRepository.findById(id)
                .map(animalMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found with id: " + id));
    }

    public Page<AnimalResponseDto> getAnimalsBySpecies(String species, Pageable pageable){
        return animalRepository.findBySpeciesContainingIgnoreCase(species, pageable)
                .map(animalMapper::toResponseDto);
    }

    public Page<AnimalResponseDto> getAnimalByName(String name, Pageable pageable){
        return animalRepository.findAllByNameContainingIgnoreCase(name, pageable)
                .map(animalMapper::toResponseDto);
    }

    @Transactional
    public AnimalResponseDto createAnimal(CreateAnimalDto createAnimalDTO){
        Animal animal = animalMapper.toEntity(createAnimalDTO);
        animal.setClient(validatorEntities.validateClient(createAnimalDTO.getClientId()));
        return animalMapper.toResponseDto(animalRepository.save(animal));
    }

    @Transactional
    public AnimalResponseDto updateAnimal(UUID id, UpdateAnimalDto updateAnimalDTO){
        Animal animal = validatorEntities.validateAnimal(id);
        animalMapper.updateAnimalFromDTO(updateAnimalDTO, animal);
        return animalMapper.toResponseDto(animalRepository.save(animal));
    }


    @Transactional
    public void deleteAnimal(UUID id){
        if(!animalRepository.existsById(id)){
            throw new ResourceNotFoundException("Animal not found with ID: " + id);
        }
        animalRepository.deleteById(id);
    }
}
