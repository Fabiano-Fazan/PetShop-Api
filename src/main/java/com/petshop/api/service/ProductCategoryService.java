package com.petshop.api.service;

import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateProductCategoryDto;
import com.petshop.api.dto.request.UpdateProductCategoryDto;
import com.petshop.api.dto.response.ProductCategoryResponseDto;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.ProductCategory;
import com.petshop.api.model.mapper.ProductCategoryMapper;
import com.petshop.api.repository.ProductCategoryRepository;
import com.petshop.api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryMapper productCategoryMapper;
    private final ProductCategoryRepository productCategoryRepository;
    private final ValidatorEntities validatorEntities;
    private final ProductRepository productRepository;


    public ProductCategoryResponseDto getProductCategoryById(UUID id){
        ProductCategory productCategory = validatorEntities.validate(id, productCategoryRepository, "Product Category");
        return productCategoryMapper.toResponseDto(productCategory);
    }

    public Page<ProductCategoryResponseDto> getAllProductCategories(Pageable pageable) {
        return productCategoryRepository.findAll(pageable)
                .map(productCategoryMapper::toResponseDto);
    }

    public Page<ProductCategoryResponseDto> getProductCategoryByNameContainingIgnoreCase(String name, Pageable pageable) {
        return productCategoryRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(productCategoryMapper::toResponseDto);
    }

    @Transactional
    public ProductCategoryResponseDto createProductCategory(CreateProductCategoryDto createProductCategoryDTO){
        ProductCategory productCategory = productCategoryMapper.toEntity(createProductCategoryDTO);
        return productCategoryMapper.toResponseDto(productCategoryRepository.save(productCategory));
    }

    @Transactional
    public ProductCategoryResponseDto updateProductCategory(UUID id, UpdateProductCategoryDto updateProductCategoryDTO) {
        ProductCategory productCategory = validatorEntities.validate(id, productCategoryRepository, "Product Category");
        productCategoryMapper.updateProductCategoryFromDto(updateProductCategoryDTO, productCategory);
        return productCategoryMapper.toResponseDto(productCategoryRepository.save(productCategory));
    }

    @Transactional
    public void deleteProductCategory(UUID id) {
        if (!productCategoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found");
            }
        if (productRepository.existsByCategoryId(id)){
            throw new BusinessException("Cannot delete this category because it is being used by products");
           }
        productCategoryRepository.deleteById(id);
    }
}




