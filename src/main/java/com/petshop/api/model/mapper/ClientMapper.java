package com.petshop.api.model.mapper;

import com.petshop.api.dto.update.UpdateClientDto;
import com.petshop.api.dto.response.ClientResponseDto;
import com.petshop.api.dto.request.CreateClientDto;
import com.petshop.api.model.entities.Client;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring", uses = {AnimalMapper.class})
public interface ClientMapper {

    @Mapping(target = "id", ignore = true)
    Client toEntity(CreateClientDto dto);


    ClientResponseDto toResponseDto(Client client);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "address", ignore = true)
    void updateClientFromDto(UpdateClientDto updateClientDto, @MappingTarget Client client);
}
