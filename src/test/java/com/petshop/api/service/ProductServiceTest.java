package com.petshop.api.service;

import com.petshop.api.domain.validator.ValidatorEntities;
import com.petshop.api.dto.request.CreateProductDto;
import com.petshop.api.dto.response.ProductResponseDto;
import com.petshop.api.dto.update.UpdateProductDto;
import com.petshop.api.exception.BusinessException;
import com.petshop.api.exception.ResourceNotFoundException;
import com.petshop.api.model.entities.Product;
import com.petshop.api.model.entities.ProductCategory;
import com.petshop.api.model.mapper.ProductMapper;
import com.petshop.api.repository.ProductCategoryRepository;
import com.petshop.api.repository.ProductRepository;
import com.petshop.api.repository.ProductSaleRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductSaleRepository productSaleRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ValidatorEntities validatorEntities;


    @Test
    @DisplayName("Should return a page of products")
    void getAllProducts_ShouldReturnPageOfProducts() {

        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product();
        ProductResponseDto responseDto = new ProductResponseDto();
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toResponseDto(product)).thenReturn(responseDto);

        Page<ProductResponseDto> result = productService.getAllProducts(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(productRepository).findAll(pageable);
    }


    @Test
    @DisplayName("Should return product when ID exists")
    void getProductById_ShouldReturnProduct_WhenIdExists() {

        UUID id = UUID.randomUUID();
        Product product = new Product();
        product.setId(id);
        ProductResponseDto responseDto = new ProductResponseDto();

        when(validatorEntities.validate(id, productRepository, "Product")).thenReturn(product);
        when(productMapper.toResponseDto(product)).thenReturn(responseDto);

        ProductResponseDto result = productService.getProductById(id);

        assertThat(result).isNotNull();
        verify(validatorEntities).validate(id, productRepository, "Product");
    }


    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID not exists")
    void getProductById_ShouldThrowException_WhenIdDoesNotExists() {

        UUID id = UUID.randomUUID();
        String expectedMessage = "Product not found";

        when(validatorEntities.validate(id, productRepository, "Product"))
                .thenThrow(new ResourceNotFoundException(expectedMessage));

        assertThatThrownBy(() -> productService.getProductById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(expectedMessage);

        verify(validatorEntities).validate(id, productRepository, "Product");
        verifyNoInteractions(productMapper);
    }


    @Test
    @DisplayName("Should return page of products by category")
    void getProductByCategory_ShouldReturnPage_WhenCategoryExists() {

        UUID categoryId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        ProductCategory category = new ProductCategory();
        category.setId(categoryId);
        Product product = new Product();
        ProductResponseDto responseDto = new ProductResponseDto();
        Page<Product> productPage = new PageImpl<>(List.of(product));


        when(validatorEntities.validate(categoryId, productCategoryRepository, "Product Category")).thenReturn(category);
        when(productRepository.findByCategory(category, pageable)).thenReturn(productPage);
        when(productMapper.toResponseDto(product)).thenReturn(responseDto);

        Page<ProductResponseDto> result = productService.getProductByCategory(categoryId, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(validatorEntities).validate(categoryId, productCategoryRepository, "Product Category");
    }


    @Test
    @DisplayName("Should return page of products when searching by name")
    void getProductByNameContainingIgnoreCase_ShouldReturnPage_WhenNameExists() {

        String name = "Shampoo";
        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product();
        ProductResponseDto responseDto = new ProductResponseDto();
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productRepository.findByNameContainingIgnoreCase(name, pageable)).thenReturn(productPage);
        when(productMapper.toResponseDto(product)).thenReturn(responseDto);

        Page<ProductResponseDto> result = productService.getProductByNameContainingIgnoreCase(name, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }


    @Test
    @DisplayName("Should create product successfully")
    void createProduct_ShouldReturnDto_WhenSuccessfully() {

        CreateProductDto createDto = new CreateProductDto();
        createDto.setCategoryId(UUID.randomUUID());
        Product product = new Product();
        ProductCategory category = new ProductCategory();
        Product savedProduct = new Product();
        savedProduct.setId(UUID.randomUUID());
        ProductResponseDto responseDto = new ProductResponseDto();

        when(productMapper.toEntity(createDto)).thenReturn(product);
        when(validatorEntities.validate(createDto.getCategoryId(), productCategoryRepository, "Product Category")).thenReturn(category);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.toResponseDto(savedProduct)).thenReturn(responseDto);

        ProductResponseDto result = productService.createProduct(createDto);

        assertThat(result).isNotNull();
        verify(productRepository).save(product);
        assertThat(product.getCategory()).isEqualTo(category);
    }


    @Test
    @DisplayName("Should update product successfully")
    void updateProduct_ShouldReturnUpdateDto_WhenSuccessful() {

        UUID id = UUID.randomUUID();
        UUID newCategoryId = UUID.randomUUID();
        UpdateProductDto updateDto = new UpdateProductDto();
        updateDto.setCategoryId(newCategoryId);
        Product product = new Product();
        product.setId(id);
        ProductCategory newCategory = new ProductCategory();
        Product savedProduct = new Product();
        ProductResponseDto responseDto = new ProductResponseDto();

        when(validatorEntities.validate(id, productRepository, "Product")).thenReturn(product);
        when(validatorEntities.validate(newCategoryId, productCategoryRepository, "Product Category")).thenReturn(newCategory);

        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.toResponseDto(savedProduct)).thenReturn(responseDto);

        ProductResponseDto result = productService.updateProduct(id, updateDto);

        assertThat(result).isNotNull();
        verify(productMapper).updateProductFromDto(updateDto, product);
        verify(productRepository).save(product);
        assertThat(product.getCategory()).isEqualTo(newCategory);
    }


    @Test
    @DisplayName("Should delete product when its not in use")
    void deleteProduct_ShouldDelete_WhenNotInUse() {

        UUID id = UUID.randomUUID();
        Product product = new Product();
        product.setId(id);

        when(validatorEntities.validate(id, productRepository, "Product")).thenReturn(product);
        when(productSaleRepository.existsByProduct(product)).thenReturn(false);

        productService.deleteProduct(id);

        verify(productRepository).delete(product);
    }


    @Test
    @DisplayName("Should throw BusinessException when trying to delete product when its in use")
    void deleteProduct_ShouldThrowException_WhenProductIsInUse() {

        UUID id = UUID.randomUUID();
        Product product = new Product();
        product.setId(id);

        when(validatorEntities.validate(id, productRepository, "Product")).thenReturn(product);
        when(productSaleRepository.existsByProduct(product)).thenReturn(true);

        assertThatThrownBy(() -> productService.deleteProduct(id))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cannot delete this product because it is being used by sales");

        verify(productRepository, never()).delete(any());
    }
}