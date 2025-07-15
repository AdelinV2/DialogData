package com.dialogdata.main.repository;

import com.dialogdata.main.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Integer> {

    @Query("SELECT pa FROM ProductAttribute pa WHERE pa.id IN (SELECT MIN(pa2.id) FROM ProductAttribute  pa2 GROUP BY pa2.name)")
    List<ProductAttribute> findAllDistinct();
}