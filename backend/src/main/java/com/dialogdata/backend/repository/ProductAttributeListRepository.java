package com.dialogdata.backend.repository;

import com.dialogdata.backend.entity.ProductAttribute;
import com.dialogdata.backend.entity.ProductAttributeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductAttributeListRepository extends JpaRepository<ProductAttributeList, Integer> {

    @Query("SELECT pa FROM ProductAttributeList pal JOIN pal.attribute pa WHERE pal.product.id = :id")
    List<ProductAttribute> findAllByProductId(Integer id);
}