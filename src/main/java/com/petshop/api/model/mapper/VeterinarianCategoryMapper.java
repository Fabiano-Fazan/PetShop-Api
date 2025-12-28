package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateVeterinarianCategoryDto;
import com.petshop.api.dto.request.UpdateVeterinarianCategoryDto;
import com.petshop.api.dto.response.VeterinarianCategoryResponseDto;
import com.petshop.api.model.entities.VeterinarianCategory;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VeterinarianCategoryMapper {

    @Mapping(target = "id", ignore = true)
    VeterinarianCategory toEntity(CreateVeterinarianCategoryDto dto);


    VeterinarianCategoryResponseDto toResponseDto(VeterinarianCategory veterinarianCategory);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateVeterinarianCategoryFromDto(UpdateVeterinarianCategoryDto updateVeterinarianCategoryDto, @MappingTarget VeterinarianCategory veterinarianCategory);
}
