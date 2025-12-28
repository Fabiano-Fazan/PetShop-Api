package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateMonetaryType;
import com.petshop.api.dto.request.UpdateMonetaryTypeDto;
import com.petshop.api.dto.response.MonetaryTypeResponseDto;
import com.petshop.api.model.entities.MonetaryType;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MonetaryTypeMapper {

    @Mapping(target = "id", ignore = true)
    MonetaryType toEntity(CreateMonetaryType dto);

    MonetaryTypeResponseDto toResponseDto(MonetaryType monetaryType);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMonetaryTypeFromDto(UpdateMonetaryTypeDto updateMonetaryTypeDto, @MappingTarget MonetaryType monetaryType);
}
