package com.dialogdata.backend.controller;

import com.dialogdata.backend.dto.ProductAttributeDto;
import com.dialogdata.backend.entity.ProductAttribute;
import com.dialogdata.backend.mapper.ProductAttributeMapper;
import com.dialogdata.backend.service.ProductAttributeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product-attribute")
public class ProductAttributeController {

    private final ProductAttributeService productAttributeService;
    private final ProductAttributeMapper productAttributeMapper;

    @Operation(summary = "Add a new product attribute")
    @ApiResponse(responseCode = "201", description = "Product attribute created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid product attribute data")
    @PostMapping
    public ResponseEntity<ProductAttributeDto> addProductAttribute(@Parameter(description = "Product attribute to be added", required = true)
                                                                @RequestBody
                                                                @Valid
                                                                ProductAttribute productAttribute) {

        ProductAttribute createdProductAttribute = productAttributeService.create(productAttribute);

        return ResponseEntity.status(201).body(productAttributeMapper.toDto(createdProductAttribute));
    }

    @Operation(summary = "Get a product attribute by ID")
    @ApiResponse(responseCode = "200", description = "Product attribute found")
    @ApiResponse(responseCode = "404", description = "Product attribute not found")
    @GetMapping("/{id}")
    public ResponseEntity<ProductAttributeDto> getProductAttributeById(@Parameter(description = "ID of the product attribute", required = true)
                                                                    @PathVariable("id") Integer id) {

        ProductAttribute productAttribute = productAttributeService.findById(id);

        if (productAttribute == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productAttributeMapper.toDto(productAttribute));
    }

    @Operation(summary = "Update a product attribute")
    @ApiResponse(responseCode = "200", description = "Product attribute updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid product attribute data")
    @ApiResponse(responseCode = "404", description = "Product attribute not found")
    @PutMapping("/{id}")
    public ResponseEntity<ProductAttributeDto> updateProductAttribute(@Parameter(description = "ID of the product attribute to be updated", required = true)
                                                                    @PathVariable("id") Integer id,
                                                                    @Parameter(description = "Updated product attribute data", required = true)
                                                                    @RequestBody
                                                                    @Valid
                                                                    ProductAttribute productAttribute) {

        ProductAttribute updatedProductAttribute = productAttributeService.update(id, productAttribute);

        if (updatedProductAttribute == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productAttributeMapper.toDto(updatedProductAttribute));
    }

    @Operation(summary = "Delete a product attribute")
    @ApiResponse(responseCode = "204", description = "Product attribute deleted successfully")
    @ApiResponse(responseCode = "404", description = "Product attribute not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductAttribute(@Parameter(description = "ID of the product attribute to be deleted", required = true)
                                                       @PathVariable("id") Integer id) {

        boolean isDeleted = productAttributeService.deleteById(id);

        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
