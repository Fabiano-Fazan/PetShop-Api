package com.petshop.api.model.mapper;

import com.petshop.api.dto.response.FinancialResponseDto;
import com.petshop.api.dto.response.ProductSaleResponseDto;
import com.petshop.api.dto.response.SaleResponseDto;
import com.petshop.api.model.entities.Client;
import com.petshop.api.model.entities.Financial;
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
    date = "2025-11-24T20:36:55-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Amazon.com Inc.)"
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
        saleResponseDto.setFinancial( financialListToFinancialResponseDtoList( sale.getFinancial() ) );

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
        if ( sale == null ) {
            return null;
        }
        Client client = sale.getClient();
        if ( client == null ) {
            return null;
        }
        UUID id = client.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String saleClientName(Sale sale) {
        if ( sale == null ) {
            return null;
        }
        Client client = sale.getClient();
        if ( client == null ) {
            return null;
        }
        String name = client.getName();
        if ( name == null ) {
            return null;
        }
        return name;
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

    protected FinancialResponseDto financialToFinancialResponseDto(Financial financial) {
        if ( financial == null ) {
            return null;
        }

        FinancialResponseDto financialResponseDto = new FinancialResponseDto();

        financialResponseDto.setDescription( financial.getDescription() );
        financialResponseDto.setAmount( financial.getAmount() );
        financialResponseDto.setDueDate( financial.getDueDate() );
        financialResponseDto.setPaymentDate( financial.getPaymentDate() );
        financialResponseDto.setInstallment( financial.getInstallment() );
        financialResponseDto.setIsPaid( financial.getIsPaid() );

        return financialResponseDto;
    }

    protected List<FinancialResponseDto> financialListToFinancialResponseDtoList(List<Financial> list) {
        if ( list == null ) {
            return null;
        }

        List<FinancialResponseDto> list1 = new ArrayList<FinancialResponseDto>( list.size() );
        for ( Financial financial : list ) {
            list1.add( financialToFinancialResponseDto( financial ) );
        }

        return list1;
    }

    private UUID productSaleProductId(ProductSale productSale) {
        if ( productSale == null ) {
            return null;
        }
        Product product = productSale.getProduct();
        if ( product == null ) {
            return null;
        }
        UUID id = product.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String productSaleProductName(ProductSale productSale) {
        if ( productSale == null ) {
            return null;
        }
        Product product = productSale.getProduct();
        if ( product == null ) {
            return null;
        }
        String name = product.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
