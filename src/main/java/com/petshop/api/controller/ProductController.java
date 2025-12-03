package com.petshop.api.controller;

import com.petshop.api.dto.request.CreateProductDto;
import com.petshop.api.dto.request.UpdateProductDto;
import com.petshop.api.dto.response.ProductResponseDto;
import com.petshop.api.model.entities.ProductCategory;
import com.petshop.api.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(Pageable pageable) {
        Page<ProductResponseDto> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/id")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable UUID id) {
        ProductResponseDto productById = productService.getProductById(id);
        return ResponseEntity.ok(productById);
    }

    @GetMapping("/category")
    public ResponseEntity<Page<ProductResponseDto>> getProductByCategory(@RequestParam UUID categoryId, Pageable pageable) {
        Page<ProductResponseDto> productsByCategory = productService.getProductByCategory(categoryId,pageable);
        return ResponseEntity.ok(productsByCategory);
    }

    @GetMapping("/name")
    public ResponseEntity<Page<ProductResponseDto>> getProductByName(@RequestParam String name, Pageable pageable) {
        Page<ProductResponseDto> productsByName = productService.getProductByName(name,pageable);
        return ResponseEntity.ok(productsByName);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody CreateProductDto createProductDTO) {
        ProductResponseDto createdProduct = productService.createProduct(createProductDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable UUID id, @Valid @RequestBody UpdateProductDto updateProductDTO) {
        ProductResponseDto updatedProduct = productService.updateProduct(id, updateProductDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
