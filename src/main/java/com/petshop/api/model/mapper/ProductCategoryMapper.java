package com.petshop.api.model.mapper;

import com.petshop.api.dto.request.CreateProductCategoryDto;
import com.petshop.api.dto.request.UpdateProductCategoryDto;
import com.petshop.api.dto.response.ProductCategoryResponseDto;
import com.petshop.api.model.entities.ProductCategory;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {

    @Mapping(target = "id", ignore = true)
    ProductCategory toEntity(CreateProductCategoryDto dto);

    ProductCategoryResponseDto toResponseDto(ProductCategory productCategory);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductCategoryFromDto(UpdateProductCategoryDto updateProductCategoryDto, @MappingTarget ProductCategory productCategory);
}
