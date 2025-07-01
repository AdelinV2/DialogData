package com.dialogdata.backend.controller;

import com.dialogdata.backend.dto.ProductDto;
import com.dialogdata.backend.entity.Product;
import com.dialogdata.backend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get a product by ID")
    @ApiResponse(responseCode = "200", description = "Product found")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@Parameter(description = "ID of the product", required = true)
                                                  @PathVariable("id") Integer id) {

        Product product = productService.findById(id);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Create a new product")
    @ApiResponse(responseCode = "201", description = "Product created")
    @ApiResponse(responseCode = "400", description = "Invalid product data")
    @PostMapping
    public ResponseEntity<Product> createProduct(@Parameter(description = "Product to create", required = true)
                                                 @RequestParam @Valid ProductDto product) {

        Product createdProduct = productService.createProduct(product.toEntity());
        // TODO save product attributes and category

        if (createdProduct == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(201).body(createdProduct);
    }

    @Operation(summary = "Get all products with pagination")
    @ApiResponse(responseCode = "200", description = "List of products retrieved")
    @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(Pageable pageable) {

        Page<Product> products = productService.getProducts(pageable);

        return ResponseEntity.ok(products);
    }
}
