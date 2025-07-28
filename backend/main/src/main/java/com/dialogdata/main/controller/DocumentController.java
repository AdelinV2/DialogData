package com.dialogdata.main.controller;

import com.dialogdata.main.dto.DocumentDto;
import com.dialogdata.main.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/document")
public class DocumentController {

    private final DocumentService documentService;

    @Operation(summary = "Get document by product ID")
    @ApiResponse(responseCode = "200", description = "Document found")
    @ApiResponse(responseCode = "404", description = "Document not found")
    @GetMapping("/product/{productId}")
    public ResponseEntity<DocumentDto> getDocumentByProductId(
            @Parameter(description = "ID of the product", required = true)
            @PathVariable("productId") Integer productId) {

        DocumentDto document = documentService.getDocumentByProductId(productId);

        if (document == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(document);
    }
}
