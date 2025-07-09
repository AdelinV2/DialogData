package com.dialogdata.main.controller;

import com.dialogdata.main.dto.ProductDto;
import com.dialogdata.main.entity.Product;
import com.dialogdata.main.mapper.ProductMapper;
import com.dialogdata.main.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Operation(summary = "Get a product by ID")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@Parameter(description = "ID of the product", required = true)
                                                     @PathVariable("id") Integer id) {

        ProductDto product = productService.findProductDtoById(id);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        product.setImages(productService.getProductImagesByProductId(id));

        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Create a new product")
    @ApiResponse(responseCode = "201", description = "Product created")
    @ApiResponse(responseCode = "400", description = "Invalid product data")
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Parameter(description = "Product to create", required = true)
                                                    @RequestBody @Valid ProductDto product) {

        return ResponseEntity.status(201).body(productService.createProduct(product));
    }

    @Operation(summary = "Get all products with pagination")
    @ApiResponse(responseCode = "200", description = "List of products retrieved")
    @ApiResponse(responseCode = "400", description = "Invalid pagination parameters, search query, or category ID")
    @GetMapping
    public ResponseEntity<Page<ProductDto>> getProducts(
            @Parameter(description = "Pagination information", required = true) Pageable pageable,
            @Parameter(description = "Search query for products", required = false)
            @RequestParam(value = "search", required = false) String search,
            @Parameter(description = "Category ID to filter products", required = false)
            @RequestParam(value = "categoryId", required = false) Integer categoryId) {

        Page<Product> products = productService.getProductsByCategoryIdAndSearch(pageable, categoryId, search);

        Page<ProductDto> productDtos = products.map(product -> {
            ProductDto dto = productMapper.toDto(product);
            dto.setImages(productService.getProductImagesByProductId(product.getId()));
            return dto;
        });

        return ResponseEntity.ok(productDtos);
    }

    @Operation(summary = "Get products by category ID with pagination")
    @ApiResponse(responseCode = "200", description = "List of products by category retrieved")
    @ApiResponse(responseCode = "400", description = "Invalid category ID or pagination parameters")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductDto>> getProductsByCategoryId(
            @Parameter(description = "ID of the category", required = true)
            @PathVariable("categoryId") Integer categoryId,
            Pageable pageable) {

        Page<Product> products = productService.getProductsByCategory(categoryId, pageable);

        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Page<ProductDto> productDtos = products.map(productMapper::toDto);

        return ResponseEntity.ok(productDtos);
    }

    @Operation(summary = "Update a product by ID")
    @ApiResponse(responseCode = "200", description = "Product updated")
    @ApiResponse(responseCode = "400", description = "Invalid product data")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @Parameter(description = "ID of the product to update", required = true)
            @PathVariable("id") Integer id,
            @Parameter(description = "Updated product data", required = true)
            @RequestBody @Valid ProductDto productDto) {

        Product updatedProduct = productService.updateProduct(id, productMapper.toEntity(productDto));

        if (updatedProduct == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productMapper.toDto(updatedProduct));
    }
}
