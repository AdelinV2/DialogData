package com.dialogdata.main.repository;

import com.dialogdata.main.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT c FROM Category c JOIN c.products p WHERE p.id = :id")
    Category findCategoryByProductId(Integer id);
}
