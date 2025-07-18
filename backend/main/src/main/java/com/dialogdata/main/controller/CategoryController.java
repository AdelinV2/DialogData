package com.dialogdata.main.controller;

import com.dialogdata.main.dto.CategoryDto;
import com.dialogdata.main.entity.Category;
import com.dialogdata.main.mapper.CategoryMapper;
import com.dialogdata.main.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Operation(summary = "Add a new category")
    @ApiResponse(responseCode = "201", description = "Category created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid category data")
    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Parameter(description = "Category object", required = true)
                                                      CategoryDto categoryDto) {

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

    @Operation(summary = "Update an existing category")
    @ApiResponse(responseCode = "200", description = "Category updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid category data")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Parameter(description = "ID of the category to update", required = true)
            @PathVariable("id") Integer id,
            @Parameter(description = "Updated category object", required = true)
            @RequestBody CategoryDto categoryDto) {

        Category updatedCategory = categoryService.updateCategory(id, categoryMapper.toEntity(categoryDto));

        if (updatedCategory == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(categoryMapper.toDto(updatedCategory));
    }

    @Operation(summary = "Delete a category")
    @ApiResponse(responseCode = "204", description = "Category deleted successfully")
    @ApiResponse(responseCode = "400", description = "Invalid category ID")
    @ApiResponse(responseCode = "404", description = "Category not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID of the category to delete", required = true)
            @PathVariable("id") Integer id) {

        boolean isDeleted = categoryService.deleteCategoryById(id);

        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
