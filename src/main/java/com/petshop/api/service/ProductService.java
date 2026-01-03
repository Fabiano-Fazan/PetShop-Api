package com.petshop.api.service;

import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateProductDto;
import com.petshop.api.dto.update.UpdateProductDto;
import com.petshop.api.dto.response.ProductResponseDto;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.model.entities.Product;
import com.petshop.api.model.mapper.ProductMapper;
import com.petshop.api.repository.ProductCategoryRepository;
import com.petshop.api.repository.ProductRepository;
import com.petshop.api.repository.ProductSaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductSaleRepository productSaleRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductMapper productMapper;
    private final ValidatorEntities validatorEntities;


    public ProductResponseDto getProductById(UUID id) {
        var product = validatorEntities.validate(id, productRepository, "Product");
        return productMapper.toResponseDto(product);
    }


    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toResponseDto);
    }

    public Page<ProductResponseDto> getProductByCategory(UUID categoryId, Pageable pageable) {
        var productCategory = validatorEntities.validate(categoryId, productCategoryRepository, "Product Category");
        return productRepository.findByCategory(productCategory, pageable)
                .map(productMapper::toResponseDto);
    }

    public Page<ProductResponseDto> getProductByNameContainingIgnoreCase(String name, Pageable pageable){
        return productRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(productMapper::toResponseDto);
    }


    @Transactional
    public ProductResponseDto createProduct(CreateProductDto dto) {
        var product = productMapper.toEntity(dto);
        product.setCategory(validatorEntities.validate(dto.getCategoryId(), productCategoryRepository, "Product Category"));
        return productMapper.toResponseDto(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDto updateProduct(UUID id, UpdateProductDto updateDto) {
        var product = validatorEntities.validate(id, productRepository, "Product");
        productMapper.updateProductFromDto(updateDto, product);
        product.setCategory(validatorEntities.validate(updateDto.getCategoryId(), productCategoryRepository, "Product Category"));
        return productMapper.toResponseDto(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(UUID id) {
        var product = validatorEntities.validate(id, productRepository, "Product");
        canDelete(product);
        productRepository.deleteById(id);
    }

    private void canDelete(Product product) {
        if (productSaleRepository.existsByProduct(product)) {
            throw new BusinessException("Cannot delete this product because it is being used by sales");
        }
    }
}
