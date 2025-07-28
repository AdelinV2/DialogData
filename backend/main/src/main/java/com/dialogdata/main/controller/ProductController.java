package com.dialogdata.main.controller;

import com.dialogdata.main.dto.CsvDto;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Create a new product")
    @ApiResponse(responseCode = "201", description = "Product created")
    @ApiResponse(responseCode = "400", description = "Invalid product data")
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Parameter(description = "Product to create", required = true)
                                                    @RequestBody @Valid ProductDto product,
                                                    @Parameter(description = "Document file", required = false)
                                                    @RequestParam(value = "file", required = false) MultipartFile file) {

        return ResponseEntity.status(201).body(productService.createProduct(product, file));
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
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @Parameter(description = "Attribute value to filter products", required = false)
            @RequestParam(value = "attributeValue", required = false) List<String> attributeValue
    ) {

        Page<ProductDto> products = productService.getProductsByCategoryIdAndSearchAndAttribute(pageable, categoryId, search, attributeValue);

        return ResponseEntity.ok(products);
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

        Page<ProductDto> products = productService.getProductsByCategory(categoryId, pageable);

        if (products.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Update a product by ID")
    @ApiResponse(responseCode = "200", description = "Product updated")
    @ApiResponse(responseCode = "400", description = "Invalid product data")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> updateProduct(
            @Parameter(description = "ID of the product to update", required = true)
            @PathVariable("id") Integer id,
            @Parameter(description = "Updated product data", required = true)
            @RequestPart("productDto") @Valid ProductDto productDto,
            @Parameter(description = "Document file for the product", required = false)
            @RequestPart(value = "file", required = false) MultipartFile file) {

        ProductDto updatedProduct = productService.updateProduct(id, productDto, file);

        if (updatedProduct == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedProduct);
    }

    @Operation(summary = "Delete a product by ID")
    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(
            @Parameter(description = "ID of the product to delete", required = true)
            @PathVariable("id") Integer id) {

        boolean deleted = productService.deleteProductById(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add products via CSV file")
    @ApiResponse(responseCode = "201", description = "Products added successfully")
    @ApiResponse(responseCode = "400", description = "Invalid CSV file format")
    @PostMapping("/csv")
    public ResponseEntity<CsvDto> addProductsFromCsv(
            @Parameter(description = "CSV file containing product data", required = true)
            @RequestParam("file") MultipartFile file) throws ExecutionException, InterruptedException {

        CompletableFuture<CsvDto> csvDto = productService.addProductsFromCsv(file);

        while (!csvDto.isDone()) {
            if (csvDto.isCompletedExceptionally()) {
                return ResponseEntity.badRequest().build();
            }
            Thread.sleep(500);
        }

        return ResponseEntity.status(201).body(csvDto.get());
    }

    @Operation(summary = "Get promoted products")
    @ApiResponse(responseCode = "200", description = "List of promoted products retrieved")
    @ApiResponse(responseCode = "404", description = "No promoted products found")
    @GetMapping("/promoted")
    public ResponseEntity<List<ProductDto>> getPromotedProducts() {

        List<ProductDto> promotedProducts = productService.getPromotedProducts();

        if (promotedProducts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(promotedProducts);
    }
}
