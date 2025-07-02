package com.dialogdata.backend.controller;

import com.dialogdata.backend.dto.CategoryDto;
import com.dialogdata.backend.entity.Category;
import com.dialogdata.backend.mapper.CategoryMapper;
import com.dialogdata.backend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Operation(summary = "Add a new category")
    @ApiResponse(responseCode = "201", description = "Category created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid category data")
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(CategoryDto categoryDto) {

        Category createdCategory = categoryService.createCategory(categoryMapper.toEntity(categoryDto));

        if (createdCategory == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(201).body(categoryMapper.toDto(createdCategory));
    }

    @Operation(summary = "Get all categories")
    @ApiResponse(responseCode = "200", description = "List of categories retrieved successfully")
    @ApiResponse(responseCode = "204", description = "No categories found")
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {

        List<Category> categories = categoryService.getAllCategories();

        if (categories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(categoryMapper.toDtoList(categories));
    }
}
