package com.dialogdata.main.repository;

import com.dialogdata.main.entity.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, Integer> {

//    @Query("SELECT pa FROM ProductAttributeValue pav JOIN pav.attribute pa WHERE pav.product.id = :id")
    List<ProductAttributeValue> findAllByProductId(@Param("id") Integer id);

    @Query("SELECT pav FROM ProductAttributeValue pav WHERE pav.attribute.id = :attributeId AND pav.attribute.name IN " +
           "(SELECT DISTINCT pav2.attribute.name FROM ProductAttributeValue pav2)")
    List<ProductAttributeValue> findAllByAttribute_IdAndUnique(@Param("attributeId") Integer attributeId);
}