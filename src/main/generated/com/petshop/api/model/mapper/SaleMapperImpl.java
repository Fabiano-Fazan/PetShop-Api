package com.petshop.api.model.mapper;

import com.petshop.api.dto.response.ProductSaleResponseDto;
import com.petshop.api.dto.response.SaleResponseDto;
import com.petshop.api.model.entities.Client;
import com.petshop.api.model.entities.Product;
import com.petshop.api.model.entities.ProductSale;
import com.petshop.api.model.entities.Sale;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-05T14:56:36-0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 21.0.8 (Amazon.com Inc.)"
)
@Component
public class SaleMapperImpl implements SaleMapper {

    @Override
    public SaleResponseDto toResponseDto(Sale sale) {
        if ( sale == null ) {
            return null;
        }

        SaleResponseDto saleResponseDto = new SaleResponseDto();

        saleResponseDto.setClientId( saleClientId( sale ) );
        saleResponseDto.setClientName( saleClientName( sale ) );
        saleResponseDto.setId( sale.getId() );
        saleResponseDto.setSaleDate( sale.getSaleDate() );
        saleResponseDto.setTotalValue( sale.getTotalValue() );
        saleResponseDto.setProductSales( productSaleListToProductSaleResponseDtoList( sale.getProductSales() ) );

        return saleResponseDto;
    }

    @Override
    public ProductSaleResponseDto toProductSaleDto(ProductSale productSale) {
        if ( productSale == null ) {
            return null;
        }

        ProductSaleResponseDto productSaleResponseDto = new ProductSaleResponseDto();

        productSaleResponseDto.setProductId( productSaleProductId( productSale ) );
        productSaleResponseDto.setProductName( productSaleProductName( productSale ) );
        productSaleResponseDto.setQuantity( productSale.getQuantity() );
        productSaleResponseDto.setUnitPrice( productSale.getUnitPrice() );

        return productSaleResponseDto;
    }

    private UUID saleClientId(Sale sale) {
        Client client = sale.getClient();
        if ( client == null ) {
            return null;
        }
        return client.getId();
    }

    private String saleClientName(Sale sale) {
        Client client = sale.getClient();
        if ( client == null ) {
            return null;
        }
        return client.getName();
    }

    protected List<ProductSaleResponseDto> productSaleListToProductSaleResponseDtoList(List<ProductSale> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductSaleResponseDto> list1 = new ArrayList<ProductSaleResponseDto>( list.size() );
        for ( ProductSale productSale : list ) {
            list1.add( toProductSaleDto( productSale ) );
        }

        return list1;
    }

    private UUID productSaleProductId(ProductSale productSale) {
        Product product = productSale.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getId();
    }

    private String productSaleProductName(ProductSale productSale) {
        Product product = productSale.getProduct();
        if ( product == null ) {
            return null;
        }
        return product.getName();
    }
}
