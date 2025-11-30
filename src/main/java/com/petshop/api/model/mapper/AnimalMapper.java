package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateAnimalDto;
import com.petshop.api.dto.request.UpdateAnimalDto;
import com.petshop.api.dto.response.AnimalResponseDto;
import com.petshop.api.model.entities.Animal;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AnimalMapper {

    @Mapping(target = "id", ignore = true)
    Animal toEntity(CreateAnimalDto createAnimalDTO);


    @Mapping(target = "clientId",source = "client.id")
    AnimalResponseDto toResponseDto(Animal animal);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateAnimalFromDTO(UpdateAnimalDto updateAnimalDTO, @MappingTarget Animal animal);
}
