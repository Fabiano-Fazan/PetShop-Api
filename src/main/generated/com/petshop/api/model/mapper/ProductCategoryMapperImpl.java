package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateProductCategoryDto;
import com.petshop.api.dto.request.UpdateProductCategoryDto;
import com.petshop.api.dto.response.ProductCategoryResponseDto;
import com.petshop.api.model.entities.ProductCategory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-09T08:23:15-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.8 (Amazon.com Inc.)"
)
@Component
public class ProductCategoryMapperImpl implements ProductCategoryMapper {

    @Override
    public ProductCategory toEntity(CreateProductCategoryDto createProductCategoryDTO) {
        if ( createProductCategoryDTO == null ) {
            return null;
        }

        ProductCategory.ProductCategoryBuilder productCategory = ProductCategory.builder();

        productCategory.name( createProductCategoryDTO.getName() );
        productCategory.description( createProductCategoryDTO.getDescription() );

        return productCategory.build();
    }

    @Override
    public ProductCategoryResponseDto toResponseDto(ProductCategory productCategory) {
        if ( productCategory == null ) {
            return null;
        }

        ProductCategoryResponseDto productCategoryResponseDto = new ProductCategoryResponseDto();

        productCategoryResponseDto.setId( productCategory.getId() );
        productCategoryResponseDto.setName( productCategory.getName() );
        productCategoryResponseDto.setDescription( productCategory.getDescription() );

        return productCategoryResponseDto;
    }

    @Override
    public void updateProductCategoryFromDTO(UpdateProductCategoryDto updateProductCategoryDTO, ProductCategory productCategory) {
        if ( updateProductCategoryDTO == null ) {
            return;
        }

        if ( updateProductCategoryDTO.getName() != null ) {
            productCategory.setName( updateProductCategoryDTO.getName() );
        }
        if ( updateProductCategoryDTO.getDescription() != null ) {
            productCategory.setDescription( updateProductCategoryDTO.getDescription() );
        }
    }
}
