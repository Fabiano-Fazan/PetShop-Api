package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateClientDto;
import com.petshop.api.dto.request.UpdateClientDto;
import com.petshop.api.dto.response.AnimalResponseDto;
import com.petshop.api.dto.response.ClientResponseDto;
import com.petshop.api.model.entities.Address;
import com.petshop.api.model.entities.Animal;
import com.petshop.api.model.entities.Client;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-24T20:36:55-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Amazon.com Inc.)"
)
@Component
public class ClientMapperImpl implements ClientMapper {

    @Autowired
    private AnimalMapper animalMapper;

    @Override
    public Client toEntity(CreateClientDto createClientDTO) {
        if ( createClientDTO == null ) {
            return null;
        }

        Client.ClientBuilder client = Client.builder();

        client.name( createClientDTO.getName() );
        client.phone( createClientDTO.getPhone() );
        client.cpf( createClientDTO.getCpf() );
        client.address( addressDataToAddress( createClientDTO.getAddress() ) );

        return client.build();
    }

    @Override
    public ClientResponseDto toResponseDto(Client client) {
        if ( client == null ) {
            return null;
        }

        ClientResponseDto clientResponseDto = new ClientResponseDto();

        clientResponseDto.setId( client.getId() );
        clientResponseDto.setName( client.getName() );
        clientResponseDto.setPhone( client.getPhone() );
        clientResponseDto.setCpf( client.getCpf() );
        clientResponseDto.setAddress( client.getAddress() );
        clientResponseDto.setAnimals( animalListToAnimalResponseDtoList( client.getAnimals() ) );

        return clientResponseDto;
    }

    @Override
    public void updateClientFromDTO(UpdateClientDto updateClientDTO, Client client) {
        if ( updateClientDTO == null ) {
            return;
        }

        if ( updateClientDTO.getName() != null ) {
            client.setName( updateClientDTO.getName() );
        }
        if ( updateClientDTO.getPhone() != null ) {
            client.setPhone( updateClientDTO.getPhone() );
        }
    }

    protected Address addressDataToAddress(CreateClientDto.AddressData addressData) {
        if ( addressData == null ) {
            return null;
        }

        Address.AddressBuilder address = Address.builder();

        address.street( addressData.getStreet() );
        address.city( addressData.getCity() );
        address.state( addressData.getState() );
        address.zipCode( addressData.getZipCode() );
        address.complement( addressData.getComplement() );

        return address.build();
    }

    protected List<AnimalResponseDto> animalListToAnimalResponseDtoList(List<Animal> list) {
        if ( list == null ) {
            return null;
        }

        List<AnimalResponseDto> list1 = new ArrayList<AnimalResponseDto>( list.size() );
        for ( Animal animal : list ) {
            list1.add( animalMapper.toResponseDto( animal ) );
        }

        return list1;
    }
}
