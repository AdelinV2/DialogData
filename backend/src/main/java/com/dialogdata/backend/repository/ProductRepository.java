package com.dialogdata.backend.repository;

import com.dialogdata.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p JOIN CategoryProductList c ON p.id = c.product.id WHERE c.category.id = :categoryId")
    Page<Product> findAllByCategoryId(Integer categoryId, Pageable pageable);
}
