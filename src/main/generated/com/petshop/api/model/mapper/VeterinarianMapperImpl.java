package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateVeterinarianDto;
import com.petshop.api.dto.request.UpdateVeterinarianDto;
import com.petshop.api.dto.response.VeterinarianResponseDto;
import com.petshop.api.model.entities.Veterinarian;
import com.petshop.api.model.entities.VeterinarianCategory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-26T19:53:32-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.8 (Amazon.com Inc.)"
)
@Component
public class VeterinarianMapperImpl implements VeterinarianMapper {

    @Override
    public Veterinarian toEntity(CreateVeterinarianDto createVeterinarianDTO) {
        if ( createVeterinarianDTO == null ) {
            return null;
        }

        Veterinarian.VeterinarianBuilder veterinarian = Veterinarian.builder();

        veterinarian.name( createVeterinarianDTO.getName() );
        veterinarian.crmv( createVeterinarianDTO.getCrmv() );
        veterinarian.phone( createVeterinarianDTO.getPhone() );
        veterinarian.email( createVeterinarianDTO.getEmail() );

        return veterinarian.build();
    }

    @Override
    public VeterinarianResponseDto toResponseDto(Veterinarian veterinarian) {
        if ( veterinarian == null ) {
            return null;
        }

        VeterinarianResponseDto veterinarianResponseDto = new VeterinarianResponseDto();

        veterinarianResponseDto.setCategory( veterinarianCategoryName( veterinarian ) );
        veterinarianResponseDto.setId( veterinarian.getId() );
        veterinarianResponseDto.setName( veterinarian.getName() );
        veterinarianResponseDto.setCrmv( veterinarian.getCrmv() );
        veterinarianResponseDto.setPhone( veterinarian.getPhone() );
        veterinarianResponseDto.setEmail( veterinarian.getEmail() );

        return veterinarianResponseDto;
    }

    @Override
    public void updateVeterinarianFromDTO(UpdateVeterinarianDto updateVeterinarianDTO, Veterinarian veterinarian) {
        if ( updateVeterinarianDTO == null ) {
            return;
        }

        if ( updateVeterinarianDTO.getName() != null ) {
            veterinarian.setName( updateVeterinarianDTO.getName() );
        }
        if ( updateVeterinarianDTO.getPhone() != null ) {
            veterinarian.setPhone( updateVeterinarianDTO.getPhone() );
        }
        if ( updateVeterinarianDTO.getEmail() != null ) {
            veterinarian.setEmail( updateVeterinarianDTO.getEmail() );
        }
    }

    private String veterinarianCategoryName(Veterinarian veterinarian) {
        VeterinarianCategory category = veterinarian.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getName();
    }
}
