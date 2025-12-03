package com.petshop.api.model.mapper;

import com.petshop.api.dto.response.ProductSaleResponseDto;
import com.petshop.api.dto.response.SaleResponseDto;
import com.petshop.api.model.entities.ProductSale;
import com.petshop.api.model.entities.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "clientName",source = "client.name")
    SaleResponseDto toResponseDto(Sale sale);


    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    ProductSaleResponseDto toProductSaleDto(ProductSale productSale);

}
