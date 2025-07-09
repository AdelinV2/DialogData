package com.dialogdata.main.repository;

import com.dialogdata.main.entity.ProductAttribute;
import com.dialogdata.main.entity.ProductAttributeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductAttributeListRepository extends JpaRepository<ProductAttributeList, Integer> {

    @Query("SELECT pa FROM ProductAttributeList pal JOIN pal.attribute pa WHERE pal.product.id = :id")
    List<ProductAttribute> findAllByProductId(@Param("id") Integer id);
}