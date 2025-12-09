package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.UpdateAddressDto;
import com.petshop.api.model.entities.Address;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-09T08:23:15-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.8 (Amazon.com Inc.)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public void updateAddressFromDTO(UpdateAddressDto updateAddressDto, Address address) {
        if ( updateAddressDto == null ) {
            return;
        }

        if ( updateAddressDto.getStreet() != null ) {
            address.setStreet( updateAddressDto.getStreet() );
        }
        if ( updateAddressDto.getCity() != null ) {
            address.setCity( updateAddressDto.getCity() );
        }
        if ( updateAddressDto.getState() != null ) {
            address.setState( updateAddressDto.getState() );
        }
        if ( updateAddressDto.getZipCode() != null ) {
            address.setZipCode( updateAddressDto.getZipCode() );
        }
        if ( updateAddressDto.getComplement() != null ) {
            address.setComplement( updateAddressDto.getComplement() );
        }
    }
}
