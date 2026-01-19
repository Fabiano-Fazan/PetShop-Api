package com.petshop.api.model.mapper;


import com.petshop.api.dto.request.CreateProductDto;
import com.petshop.api.dto.update.UpdateProductDto;
import com.petshop.api.dto.response.ProductResponseDto;
import com.petshop.api.model.entities.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "quantityInStock", defaultValue= "0")
    Product toEntity(CreateProductDto dto);


    @Mapping(target = "category", source = "category.name")
    ProductResponseDto toResponseDto(Product product);



    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromDto(UpdateProductDto updateProductDto, @MappingTarget Product product);
}

