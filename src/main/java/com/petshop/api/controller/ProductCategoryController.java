package com.petshop.api.controller;

import com.petshop.api.dto.request.CreateProductCategoryDto;
import com.petshop.api.dto.update.UpdateProductCategoryDto;
import com.petshop.api.dto.response.ProductCategoryResponseDto;
import com.petshop.api.service.ProductCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product-categories")
@RequiredArgsConstructor
public class ProductCategoryController {
    private  final ProductCategoryService productCategoryService;

    @GetMapping
    public ResponseEntity<Page<ProductCategoryResponseDto>> getAllProductCategories(Pageable pageable) {
        Page<ProductCategoryResponseDto> allProductCategories = productCategoryService.getAllProductCategories(pageable);
        return ResponseEntity.ok(allProductCategories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryResponseDto> getProductCategoryById(@PathVariable UUID id){
        ProductCategoryResponseDto productCategoryById = productCategoryService.getProductCategoryById(id);
        return ResponseEntity.ok(productCategoryById);
    }

    @GetMapping("/name")
    public ResponseEntity<Page<ProductCategoryResponseDto>> getProductCategoriesByName(@RequestParam String name, Pageable pageable) {
        Page<ProductCategoryResponseDto> productCategoriesByName = productCategoryService.getProductCategoryByNameContainingIgnoreCase(name, pageable);
        return ResponseEntity.ok(productCategoriesByName);
    }

    @PostMapping
    public ResponseEntity<ProductCategoryResponseDto> createProductCategory(@Valid @RequestBody CreateProductCategoryDto createProductCategoryDTO){
        ProductCategoryResponseDto createdProductCategory = productCategoryService.createProductCategory(createProductCategoryDTO);
        return new ResponseEntity<>(createdProductCategory, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductCategoryResponseDto> updateProductCategory(@PathVariable UUID id, @Valid @RequestBody UpdateProductCategoryDto updateProductCategoryDTO){
        ProductCategoryResponseDto updatedProductCategory = productCategoryService.updateProductCategory(id, updateProductCategoryDTO);
        return ResponseEntity.ok(updatedProductCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductCategory(@PathVariable UUID id){
        productCategoryService.deleteProductCategory(id);
        return ResponseEntity.noContent().build();
    }
}
