package com.dialogdata.main.repository;

import com.dialogdata.main.entity.CategoryProductList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryProductListRepository extends JpaRepository<CategoryProductList, Integer> {

}
