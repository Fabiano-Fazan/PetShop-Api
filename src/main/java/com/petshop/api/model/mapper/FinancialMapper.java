package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateFinancialDto;
import com.petshop.api.dto.request.CreateFinancialPaymentDto;
import com.petshop.api.dto.response.FinancialResponseDto;
import com.petshop.api.model.entities.Financial;
import com.petshop.api.model.entities.FinancialPayment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface FinancialMapper {

    @Mapping(target = "id", ignore = true)
    Financial toEntity(CreateFinancialDto createFinancialDto);

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "name", source = "client.name")
    @Mapping(target = "saleId", source = "sale.id")
    @Mapping(target = "monetaryType", source = "monetaryType.name")
    FinancialResponseDto toResponseDto(Financial financial);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "financial", ignore = true)
    @Mapping(target = "monetaryType", ignore = true)
    FinancialPayment toPaymentEntity(CreateFinancialPaymentDto createFinancialPaymentDto);

}
