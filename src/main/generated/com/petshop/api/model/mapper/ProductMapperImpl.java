package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateProductDto;
import com.petshop.api.dto.request.UpdateProductDto;
import com.petshop.api.dto.response.ProductResponseDto;
import com.petshop.api.model.entities.Product;
import com.petshop.api.model.entities.ProductCategory;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-04T21:26:31-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.8 (Amazon.com Inc.)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product toEntity(CreateProductDto createProductDTO) {
        if ( createProductDTO == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        if ( createProductDTO.getQuantityInStock() != null ) {
            product.quantityInStock( createProductDTO.getQuantityInStock() );
        }
        else {
            product.quantityInStock( 0 );
        }
        product.name( createProductDTO.getName() );
        product.description( createProductDTO.getDescription() );
        product.price( createProductDTO.getPrice() );

        return product.build();
    }

    @Override
    public ProductResponseDto toResponseDto(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponseDto productResponseDto = new ProductResponseDto();

        productResponseDto.setCategory( productCategoryName( product ) );
        productResponseDto.setId( product.getId() );
        productResponseDto.setName( product.getName() );
        productResponseDto.setDescription( product.getDescription() );
        productResponseDto.setPrice( product.getPrice() );
        productResponseDto.setQuantityInStock( product.getQuantityInStock() );

        return productResponseDto;
    }

    @Override
    public void updateProductFromDTO(UpdateProductDto updateProductDTO, Product product) {
        if ( updateProductDTO == null ) {
            return;
        }

        if ( updateProductDTO.getName() != null ) {
            product.setName( updateProductDTO.getName() );
        }
        if ( updateProductDTO.getDescription() != null ) {
            product.setDescription( updateProductDTO.getDescription() );
        }
        if ( updateProductDTO.getPrice() != null ) {
            product.setPrice( updateProductDTO.getPrice() );
        }
    }

    private String productCategoryName(Product product) {
        ProductCategory category = product.getCategory();
        if ( category == null ) {
            return null;
        }
        return category.getName();
    }
}
