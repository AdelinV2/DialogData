package com.dialogdata.main.service;

import com.dialogdata.main.entity.Product;
import com.dialogdata.main.entity.ProductAttribute;
import com.dialogdata.main.entity.ProductAttributeList;
import com.dialogdata.main.repository.ProductAttributeListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAttributeListService {

    private final ProductAttributeListRepository productAttributeListRepository;

    public ProductAttributeList create(ProductAttribute productAttribute, Product product) {

        ProductAttributeList productAttributeList = new ProductAttributeList();

        productAttributeList.setProduct(product);
        productAttributeList.setAttribute(productAttribute);

        return productAttributeListRepository.save(productAttributeList);
    }

    public ProductAttributeList findById(Integer id) {
        return productAttributeListRepository.findById(id).orElse(null);
    }

    public ProductAttributeList update(Integer id, ProductAttributeList productAttributeList) {

        ProductAttributeList existingProductAttributeList = productAttributeListRepository.findById(id).orElse(null);

        if (existingProductAttributeList == null) {
            return null;
        }

        productAttributeList.setId(id);
        return productAttributeListRepository.save(productAttributeList);
    }

    public boolean deleteById(Integer id) {

        ProductAttributeList productAttributeList = productAttributeListRepository.findById(id).orElse(null);

        if (productAttributeList == null) {
            return false;
        }

        productAttributeListRepository.delete(productAttributeList);
        return true;
    }

    public List<ProductAttribute> findByProductId(Integer id) {
        return productAttributeListRepository.findAllByProductId(id);
    }
}
