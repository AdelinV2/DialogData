package com.dialogdata.backend.repository;

import com.dialogdata.backend.entity.Category;
import com.dialogdata.backend.entity.CategoryProductList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryProductListRepository extends JpaRepository<CategoryProductList, Integer> {

}
