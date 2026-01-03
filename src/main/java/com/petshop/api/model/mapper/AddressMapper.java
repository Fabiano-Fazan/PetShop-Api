package com.petshop.api.model.mapper;

import com.petshop.api.dto.update.UpdateAddressDto;
import com.petshop.api.model.entities.Address;
import jakarta.validation.Valid;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddressFromDto(@Valid UpdateAddressDto updateAddressDto, @MappingTarget Address address);
}
