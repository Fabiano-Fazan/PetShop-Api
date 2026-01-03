package com.petshop.api.service;

import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.update.UpdateAnimalDto;
import com.petshop.api.dto.response.AnimalResponseDto;
import com.petshop.api.dto.request.CreateAnimalDto;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.model.entities.Animal;
import com.petshop.api.model.mapper.AnimalMapper;
import com.petshop.api.repository.AnimalRepository;
import com.petshop.api.repository.ClientRepository;
import com.petshop.api.repository.MedicalAppointmentRepository;
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
    private final ClientRepository clientRepository;
    private final MedicalAppointmentRepository medicalAppointmentRepository;
    private final AnimalMapper animalMapper;
    private final ValidatorEntities validatorEntities;


    public Page<AnimalResponseDto> getAllAnimals(Pageable pageable){
        return animalRepository.findAll(pageable)
                .map(animalMapper::toResponseDto);
    }

    public AnimalResponseDto getAnimalById(UUID id) {
        var animal = validatorEntities.validate(id, animalRepository, "Animal");
        return animalMapper.toResponseDto(animal);
    }

    public Page<AnimalResponseDto> getAnimalsBySpecies(String species, Pageable pageable){
        return animalRepository.findBySpeciesContainingIgnoreCase(species, pageable)
                .map(animalMapper::toResponseDto);
    }

    public Page<AnimalResponseDto> getAnimalByNameContainingIgnoreCase(String name, Pageable pageable){
        return animalRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(animalMapper::toResponseDto);
    }

    @Transactional
    public AnimalResponseDto createAnimal(CreateAnimalDto dto){
        var animal = animalMapper.toEntity(dto);
        animal.setClient(validatorEntities.validate(dto.getClientId(),clientRepository, "Client"));
        return animalMapper.toResponseDto(animalRepository.save(animal));
    }

    @Transactional
    public AnimalResponseDto updateAnimal(UUID id, UpdateAnimalDto updateDto){
        var animal = validatorEntities.validate(id, animalRepository, "Animal");
        animalMapper.updateAnimalFromDto(updateDto, animal);
        return animalMapper.toResponseDto(animalRepository.save(animal));
    }

    @Transactional
    public void deleteAnimal(UUID id) {
        var animal = validatorEntities.validate(id, animalRepository, "Animal");
        canDelete(animal);
        animalRepository.delete(animal);
    }

    private void canDelete(Animal animal) {
        if (medicalAppointmentRepository.existsByAnimal(animal)){
            throw new BusinessException("Cannot delete this animal because it is being used by medical appointments");
        }
    }
}
