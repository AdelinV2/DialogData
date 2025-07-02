package com.dialogdata.backend.service;

import com.dialogdata.backend.entity.Category;
import com.dialogdata.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Integer id, Category category) {

        Category existingCategory = categoryRepository.findById(id).orElse(null);

        if (existingCategory == null) {
            return null;
        }

        category.setId(id);

        return categoryRepository.save(category);
    }

    public boolean deleteCategoryById(Integer id) {
        Category category = categoryRepository.findById(id).orElse(null);

        if (category == null) {
            return false;
        }

        categoryRepository.delete(category);

        return true;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
