package com.dialogdata.main.service;

import com.dialogdata.main.dto.ProductAttributeValueDto;
import com.dialogdata.main.entity.Product;
import com.dialogdata.main.entity.ProductAttribute;
import com.dialogdata.main.entity.ProductAttributeValue;
import com.dialogdata.main.mapper.ProductAttributeValueMapper;
import com.dialogdata.main.repository.ProductAttributeValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.invoke.ParameterValueMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAttributeValueService {

    private final ProductAttributeValueRepository productAttributeListRepository;
    private final ProductAttributeValueMapper productAttributeValueMapper;

    public ProductAttributeValue create(ProductAttributeValueDto productAttributeValueDto) {

        return productAttributeListRepository.save(productAttributeValueMapper.toEntity(productAttributeValueDto));
    }

    public ProductAttributeValue findById(Integer id) {
        return productAttributeListRepository.findById(id).orElse(null);
    }

    public ProductAttributeValue update(Integer id, ProductAttributeValue productAttributeList) {

        ProductAttributeValue existingProductAttributeList = productAttributeListRepository.findById(id).orElse(null);

        if (existingProductAttributeList == null) {
            return null;
        }

        productAttributeList.setId(id);
        return productAttributeListRepository.save(productAttributeList);
    }

    public boolean deleteById(Integer id) {

        ProductAttributeValue productAttributeList = productAttributeListRepository.findById(id).orElse(null);

        if (productAttributeList == null) {
            return false;
        }

        productAttributeListRepository.delete(productAttributeList);
        return true;
    }

    public List<ProductAttributeValue> findByProductId(Integer id) {
        return productAttributeListRepository.findAllByProductId(id);
    }

    public List<ProductAttributeValueDto> findAllByAttributeIdAndUnique(Integer attributeId) {

        List<ProductAttributeValue> attributeValues = productAttributeListRepository.findAllByAttribute_IdAndUnique(attributeId);
        return productAttributeValueMapper.toDtoList(attributeValues);
    }
}
