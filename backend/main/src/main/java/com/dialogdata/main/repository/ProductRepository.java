package com.dialogdata.main.repository;

import com.dialogdata.main.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p JOIN CategoryProductList c ON p.id = c.product.id WHERE c.category.id = :categoryId")
    Page<Product> findAllByCategoryId(Integer categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN CategoryProductList c ON p.id = c.product.id" +
            " WHERE c.category.id = :categoryId AND LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Product> findAllByCategoryIdAndNameContainingIgnoreCase(Integer categoryId, String search, Pageable pageable);

    Page<Product> findAllByNameContainingIgnoreCase(String search, Pageable pageable);
}
