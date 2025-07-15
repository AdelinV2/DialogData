package com.dialogdata.main.controller;

import com.dialogdata.main.dto.ProductAttributeValueDto;
import com.dialogdata.main.service.ProductAttributeValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/product-attribute-value")
@RestController
public class ProductAttributeValueController {

    private final ProductAttributeValueService productAttributeValueService;

    @Operation(summary = "Get product attribute values by attribute ID")
    @ApiResponse(responseCode = "200", description = "Product attribute values retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Product attribute values not found")
    @GetMapping("/{attributeId}")
    public ResponseEntity<List<ProductAttributeValueDto>> getProductAttributeValuesByAttributeId(
            @Parameter(description = "ID of the product attribute", required = true)
            @PathVariable("attributeId") Integer attributeId) {

        return ResponseEntity.ok(productAttributeValueService.findAllByAttributeIdAndUnique(attributeId));
    }
}
