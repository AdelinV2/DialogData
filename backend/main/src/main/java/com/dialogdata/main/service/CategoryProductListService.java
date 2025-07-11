package com.dialogdata.main.service;

import com.dialogdata.main.entity.Category;
import com.dialogdata.main.entity.CategoryProductList;
import com.dialogdata.main.entity.Product;
import com.dialogdata.main.repository.CategoryProductListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryProductListService {

    private final CategoryProductListRepository categoryProductListRepository;

    public CategoryProductList createCategoryProductList(Category category, Product product) {

        CategoryProductList categoryProductList = new CategoryProductList();

        categoryProductList.setCategory(category);
        categoryProductList.setProduct(product);

        return categoryProductListRepository.save(categoryProductList);
    }

    public CategoryProductList getCategoryProductListById(Integer id) {
        return categoryProductListRepository.findById(id).orElse(null);
    }

    public CategoryProductList updateCategoryProductList(Integer id, CategoryProductList categoryProductList) {

        CategoryProductList existingCategoryProductList = categoryProductListRepository.findById(id).orElse(null);

        if (existingCategoryProductList == null) {
            return null;
        }

        categoryProductList.setId(id);

        return categoryProductListRepository.save(categoryProductList);
    }

    public boolean deleteCategoryProductListById(Integer id) {

        CategoryProductList categoryProductList = categoryProductListRepository.findById(id).orElse(null);

        if (categoryProductList == null) {
            return false;
        }

        categoryProductListRepository.delete(categoryProductList);

        return true;
    }
}
