package com.dialogdata.backend.repository;

import com.dialogdata.backend.entity.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, Integer> {
}