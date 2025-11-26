package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.UpdateClientDto;
import com.petshop.api.model.entities.Address;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-25T21:00:21-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Amazon.com Inc.)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public void updateAddressFromDTO(UpdateClientDto.AddressData addressDataUpdate, Address address) {
        if ( addressDataUpdate == null ) {
            return;
        }

        if ( addressDataUpdate.getStreet() != null ) {
            address.setStreet( addressDataUpdate.getStreet() );
        }
        if ( addressDataUpdate.getCity() != null ) {
            address.setCity( addressDataUpdate.getCity() );
        }
        if ( addressDataUpdate.getState() != null ) {
            address.setState( addressDataUpdate.getState() );
        }
        if ( addressDataUpdate.getZipCode() != null ) {
            address.setZipCode( addressDataUpdate.getZipCode() );
        }
        if ( addressDataUpdate.getComplement() != null ) {
            address.setComplement( addressDataUpdate.getComplement() );
        }
    }
}
