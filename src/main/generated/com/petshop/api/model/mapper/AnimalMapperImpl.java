package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateAnimalDto;
import com.petshop.api.dto.response.AnimalResponseDto;
import com.petshop.api.model.entities.Animal;
import com.petshop.api.model.entities.Client;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-24T20:36:54-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Amazon.com Inc.)"
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

    private UUID animalClientId(Animal animal) {
        if ( animal == null ) {
            return null;
        }
        Client client = animal.getClient();
        if ( client == null ) {
            return null;
        }
        UUID id = client.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
