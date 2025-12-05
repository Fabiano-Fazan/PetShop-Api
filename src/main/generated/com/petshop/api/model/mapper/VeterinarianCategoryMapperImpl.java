package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateVeterinarianCategoryDto;
import com.petshop.api.dto.request.UpdateVeterinarianCategoryDto;
import com.petshop.api.dto.response.VeterinarianCategoryResponseDto;
import com.petshop.api.model.entities.VeterinarianCategory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-04T21:26:31-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.8 (Amazon.com Inc.)"
)
@Component
public class VeterinarianCategoryMapperImpl implements VeterinarianCategoryMapper {

    @Override
    public VeterinarianCategory toEntity(CreateVeterinarianCategoryDto createVeterinarianCategoryDTO) {
        if ( createVeterinarianCategoryDTO == null ) {
            return null;
        }

        VeterinarianCategory.VeterinarianCategoryBuilder veterinarianCategory = VeterinarianCategory.builder();

        veterinarianCategory.name( createVeterinarianCategoryDTO.getName() );
        veterinarianCategory.description( createVeterinarianCategoryDTO.getDescription() );

        return veterinarianCategory.build();
    }

    @Override
    public VeterinarianCategoryResponseDto toResponseDto(VeterinarianCategory veterinarianCategory) {
        if ( veterinarianCategory == null ) {
            return null;
        }

        VeterinarianCategoryResponseDto veterinarianCategoryResponseDto = new VeterinarianCategoryResponseDto();

        if ( veterinarianCategory.getId() != null ) {
            veterinarianCategoryResponseDto.setId( veterinarianCategory.getId().toString() );
        }
        veterinarianCategoryResponseDto.setName( veterinarianCategory.getName() );
        veterinarianCategoryResponseDto.setDescription( veterinarianCategory.getDescription() );

        return veterinarianCategoryResponseDto;
    }

    @Override
    public void updateVeterinarianCategoryFromDTO(UpdateVeterinarianCategoryDto updateVeterinarianCategoryDto, VeterinarianCategory veterinarianCategory) {
        if ( updateVeterinarianCategoryDto == null ) {
            return;
        }

        if ( updateVeterinarianCategoryDto.getName() != null ) {
            veterinarianCategory.setName( updateVeterinarianCategoryDto.getName() );
        }
        if ( updateVeterinarianCategoryDto.getDescription() != null ) {
            veterinarianCategory.setDescription( updateVeterinarianCategoryDto.getDescription() );
        }
    }
}
