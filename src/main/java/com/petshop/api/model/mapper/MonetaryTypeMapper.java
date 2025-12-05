package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateMonetaryType;
import com.petshop.api.dto.response.MonetaryTypeResponseDto;
import com.petshop.api.model.entities.MonetaryType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MonetaryTypeMapper {

    @Mapping(target = "id", ignore = true)
    MonetaryType toEntity(CreateMonetaryType createMonetaryTypeDTO);

    MonetaryTypeResponseDto toResponseDto(MonetaryType monetaryType);
}
