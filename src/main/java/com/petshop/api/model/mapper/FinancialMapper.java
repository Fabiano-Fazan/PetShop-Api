package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateFinancialDto;
import com.petshop.api.dto.response.FinancialResponseDto;
import com.petshop.api.model.entities.Financial;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FinancialMapper {

    @Mapping(target = "id", ignore = true)
    Financial toEntity(CreateFinancialDto createFinancialDto);

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "saleId", source = "sale.id")
    @Mapping(target = "monetaryType", source = "monetaryType.name")
    FinancialResponseDto toResponseDto(Financial financial);
}
