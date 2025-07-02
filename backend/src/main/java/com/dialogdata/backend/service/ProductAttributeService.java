package com.dialogdata.backend.service;

import com.dialogdata.backend.entity.ProductAttribute;
import com.dialogdata.backend.repository.ProductAttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductAttributeService {

    private final ProductAttributeRepository productAttributeRepository;

    public ProductAttribute create(ProductAttribute productAttribute) {
        return productAttributeRepository.save(productAttribute);
    }

    public ProductAttribute findById(Integer id) {
        return productAttributeRepository.findById(id).orElse(null);
    }

    public ProductAttribute update(Integer id, ProductAttribute productAttribute) {

        ProductAttribute existingProductAttribute = productAttributeRepository.findById(id).orElse(null);

        if (existingProductAttribute == null) {
            return null;
        }

        productAttribute.setId(id);
        return productAttributeRepository.save(productAttribute);
    }

    public boolean deleteById(Integer id) {

        ProductAttribute productAttribute = productAttributeRepository.findById(id).orElse(null);

        if (productAttribute == null) {
            return false;
        }

        productAttributeRepository.delete(productAttribute);
        return true;
    }
}
