package com.petshop.api.service;

import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateProductCategoryDto;

import com.petshop.api.dto.response.ProductCategoryResponseDto;
import com.petshop.api.dto.update.UpdateProductCategoryDto;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.ProductCategory;
import com.petshop.api.model.mapper.ProductCategoryMapper;
import com.petshop.api.repository.ProductCategoryRepository;
import com.petshop.api.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductCategoryServiceTest {

    @InjectMocks
    private ProductCategoryService productCategoryService;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ProductCategoryMapper productCategoryMapper;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ValidatorEntities validatorEntities;

    @Test
    @DisplayName("Should return a page of product categories")
    void getAllProductCategories_ShouldReturnPageOfProductCategories(){

        Pageable pageable = PageRequest.of(0,10);
        ProductCategory productCategory = new ProductCategory();
        ProductCategoryResponseDto responseDto = new ProductCategoryResponseDto();
        Page<ProductCategory> productCategoryPage = new PageImpl<>(List.of(productCategory));

        when(productCategoryRepository.findAll(pageable)).thenReturn(productCategoryPage);
        when(productCategoryMapper.toResponseDto(productCategory)).thenReturn(responseDto);

        Page<ProductCategoryResponseDto> result = productCategoryService.getAllProductCategories(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(productCategoryRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return product category when ID exists")
    void getProductCategoryById_ShouldReturnProductCategory_WhenIdExists(){

        UUID id = UUID.randomUUID();
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(id);
        ProductCategoryResponseDto responseDto = new ProductCategoryResponseDto();

        when(validatorEntities.validate(id, productCategoryRepository, "Product category")).thenReturn(productCategory);
        when(productCategoryMapper.toResponseDto(productCategory)).thenReturn(responseDto);

        ProductCategoryResponseDto result = productCategoryService.getProductCategoryById(id);

        assertThat(result).isNotNull();
        verify(validatorEntities).validate(id, productCategoryRepository, "Product category");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID not exists")
    void getProductCategoryById_ShouldThrowException_WhenIdDoesNotExists(){

        UUID id = UUID.randomUUID();
        String expectedMessage = "Product category not found";

        when(validatorEntities.validate(id, productCategoryRepository, "Product category"))
                .thenThrow(new ResourceNotFoundException(expectedMessage));

        assertThatThrownBy(() -> productCategoryService.getProductCategoryById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(expectedMessage);

        verify(validatorEntities).validate(id, productCategoryRepository, "Product category");
        verifyNoInteractions(productCategoryMapper);
    }

    @Test
    @DisplayName("Should return page of product categories when searching by name")
    void getProductCategoryByName_ShouldReturnPage_WhenNameExistis(){

        String name = "Toys";
        Pageable pageable = PageRequest.of(0,10);
        ProductCategory productCategory = new ProductCategory();
        ProductCategoryResponseDto responseDto = new ProductCategoryResponseDto();
        Page<ProductCategory> productCategoryPage = new PageImpl<>(List.of(productCategory));

        when(productCategoryRepository.findByNameContainingIgnoreCase(name, pageable)).thenReturn(productCategoryPage);
        when(productCategoryMapper.toResponseDto(productCategory)).thenReturn(responseDto);

        Page<ProductCategoryResponseDto> result = productCategoryService.getProductCategoryByNameContainingIgnoreCase(name, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("Should create product category successfully")
    void createProductCategory_ShouldReturnDto_WhenSuccessfully(){

        CreateProductCategoryDto createDto = new CreateProductCategoryDto();
        ProductCategory productCategory = new ProductCategory();
        ProductCategory savedProductCategory = new ProductCategory();
        savedProductCategory.setId(UUID.randomUUID());
        ProductCategoryResponseDto responseDto = new ProductCategoryResponseDto();

        when(productCategoryMapper.toEntity(createDto)).thenReturn(productCategory);
        when(productCategoryRepository.save(productCategory)).thenReturn(savedProductCategory);
        when(productCategoryMapper.toResponseDto(savedProductCategory)).thenReturn(responseDto);

        ProductCategoryResponseDto result = productCategoryService.createProductCategory(createDto);

        assertThat(result).isNotNull();
        verify(productCategoryRepository).save(productCategory);
    }

    @Test
    @DisplayName("Should update product category successfully")
    void updateProductCategory_ShouldReturnUpdateDto_WhenSuccessful(){

        UUID id = UUID.randomUUID();
        UpdateProductCategoryDto updateDto = new UpdateProductCategoryDto();
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(id);
        ProductCategory savedProductCategory = new ProductCategory();
        ProductCategoryResponseDto responseDto = new ProductCategoryResponseDto();

        when(validatorEntities.validate(id, productCategoryRepository, "Product category")).thenReturn(productCategory);
        when(productCategoryRepository.save(productCategory)).thenReturn(savedProductCategory);
        when(productCategoryMapper.toResponseDto(savedProductCategory)).thenReturn(responseDto);

        ProductCategoryResponseDto result = productCategoryService.updateProductCategory(id, updateDto);

        assertThat(result).isNotNull();
        verify(productCategoryMapper).updateProductCategoryFromDto(updateDto, productCategory);
        verify(productCategoryRepository).save(productCategory);
    }

    @Test
    @DisplayName("Should delete product category when its not in use")
    void deleteProductCategory_ShouldDelete_WhenNotInUse(){
        UUID id = UUID.randomUUID();
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(id);

        when(validatorEntities.validate(id, productCategoryRepository, "Product category")).thenReturn(productCategory);
        when(productRepository.existsByCategory(productCategory)).thenReturn(false);

        productCategoryService.deleteProductCategory(id);

        verify(productCategoryRepository).delete(productCategory);
    }

    @Test
    @DisplayName("Should throw BusinessException when trying to delete product category when its in use")
    void deleteProductCategory_ShouldThrowException_WhenProductCategoryIsInUse(){

        UUID id = UUID.randomUUID();
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(id);

        when(validatorEntities.validate(id, productCategoryRepository, "Product category")).thenReturn(productCategory);
        when(productRepository.existsByCategory(productCategory)).thenReturn(true);

        assertThatThrownBy(() -> productCategoryService.deleteProductCategory(id))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cannot delete this category because it is being used by some products");

        verify(productCategoryRepository, never()).delete(any());
    }
}