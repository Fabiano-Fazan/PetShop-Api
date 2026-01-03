package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateVeterinarianDto;
import com.petshop.api.dto.update.UpdateVeterinarianDto;
import com.petshop.api.dto.response.VeterinarianResponseDto;
import com.petshop.api.model.entities.Veterinarian;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VeterinarianMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Veterinarian toEntity(CreateVeterinarianDto dto);


    @Mapping(target = "category", source = "category.name")
    VeterinarianResponseDto toResponseDto(Veterinarian veterinarian);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateVeterinarianFromDto(UpdateVeterinarianDto updateVeterinarianDto, @MappingTarget Veterinarian veterinarian);
}
