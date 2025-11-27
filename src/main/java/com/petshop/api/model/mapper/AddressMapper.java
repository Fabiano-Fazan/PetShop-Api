package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.UpdateAddressDto;
import com.petshop.api.model.entities.Address;
import jakarta.validation.Valid;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddressFromDTO(@Valid UpdateAddressDto updateAddressDto, @MappingTarget Address address);
}
