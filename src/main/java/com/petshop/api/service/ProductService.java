package com.petshop.api.service;

import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateProductDto;
import com.petshop.api.dto.request.UpdateProductDto;
import com.petshop.api.dto.response.ProductResponseDto;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.Product;
import com.petshop.api.model.entities.ProductCategory;
import com.petshop.api.model.mapper.ProductMapper;
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
    private final ProductMapper productMapper;
    private final ValidatorEntities validatorEntities;
    private final ProductSaleRepository productSaleRepository;


    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toResponseDto);
    }

    public Page<ProductResponseDto> getProductByCategory(UUID categoryId, Pageable pageable) {
        ProductCategory productCategory = validatorEntities.validateProductCategory(categoryId);
        return productRepository.findByCategory(productCategory, pageable)
                .map(productMapper::toResponseDto);
    }

    public Page<ProductResponseDto> getProductByName(String name, Pageable pageable){
        return productRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(productMapper::toResponseDto);
    }

    public ProductResponseDto getProductById(UUID id) {
        return productRepository.findById(id)
                .map(productMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Transactional
    public ProductResponseDto createProduct(CreateProductDto createProductDTO) {
        Product product = productMapper.toEntity(createProductDTO);
        product.setCategory(validatorEntities.validateProductCategory(createProductDTO.getCategoryId()));
        return productMapper.toResponseDto(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDto updateProduct(UUID id, UpdateProductDto updateProductDTO) {
        Product product = validatorEntities.validateProduct(id);
        productMapper.updateProductFromDTO(updateProductDTO, product);
        product.setCategory(validatorEntities.validateProductCategory(updateProductDTO.getCategoryId()));
        return productMapper.toResponseDto(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with ID: " + id);
            }
        if (productSaleRepository.existsByProductId(id)){
            throw new BusinessException("Cannot delete this product because it is being used in sales");
           }
        productRepository.deleteById(id);
    }
}
