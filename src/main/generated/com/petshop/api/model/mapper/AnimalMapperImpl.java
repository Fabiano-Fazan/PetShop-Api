package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateAnimalDto;
import com.petshop.api.dto.request.UpdateAnimalDto;
import com.petshop.api.dto.response.AnimalResponseDto;
import com.petshop.api.model.entities.Animal;
import com.petshop.api.model.entities.Client;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-04T21:26:31-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.8 (Amazon.com Inc.)"
)
@Component
public class AnimalMapperImpl implements AnimalMapper {

    @Override
    public Animal toEntity(CreateAnimalDto createAnimalDTO) {
        if ( createAnimalDTO == null ) {
            return null;
        }

        Animal.AnimalBuilder animal = Animal.builder();

        animal.name( createAnimalDTO.getName() );
        animal.species( createAnimalDTO.getSpecies() );
        animal.breed( createAnimalDTO.getBreed() );
        animal.birthDate( createAnimalDTO.getBirthDate() );

        return animal.build();
    }

    @Override
    public AnimalResponseDto toResponseDto(Animal animal) {
        if ( animal == null ) {
            return null;
        }

        AnimalResponseDto animalResponseDto = new AnimalResponseDto();

        animalResponseDto.setClientId( animalClientId( animal ) );
        animalResponseDto.setId( animal.getId() );
        animalResponseDto.setName( animal.getName() );
        animalResponseDto.setSpecies( animal.getSpecies() );
        animalResponseDto.setBirthDate( animal.getBirthDate() );
        animalResponseDto.setBreed( animal.getBreed() );

        return animalResponseDto;
    }

    @Override
    public void updateAnimalFromDTO(UpdateAnimalDto updateAnimalDTO, Animal animal) {
        if ( updateAnimalDTO == null ) {
            return;
        }

        if ( updateAnimalDTO.getName() != null ) {
            animal.setName( updateAnimalDTO.getName() );
        }
        if ( updateAnimalDTO.getSpecies() != null ) {
            animal.setSpecies( updateAnimalDTO.getSpecies() );
        }
        if ( updateAnimalDTO.getBreed() != null ) {
            animal.setBreed( updateAnimalDTO.getBreed() );
        }
        if ( updateAnimalDTO.getBirthDate() != null ) {
            animal.setBirthDate( updateAnimalDTO.getBirthDate() );
        }
    }

    private UUID animalClientId(Animal animal) {
        Client client = animal.getClient();
        if ( client == null ) {
            return null;
        }
        return client.getId();
    }
}
