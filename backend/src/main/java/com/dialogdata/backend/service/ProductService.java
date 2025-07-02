package com.dialogdata.backend.service;

import com.dialogdata.backend.dto.CategoryDto;
import com.dialogdata.backend.dto.ProductDto;
import com.dialogdata.backend.entity.Product;
import com.dialogdata.backend.entity.ProductAttribute;
import com.dialogdata.backend.mapper.CategoryMapper;
import com.dialogdata.backend.mapper.ProductAttributeMapper;
import com.dialogdata.backend.mapper.ProductMapper;
import com.dialogdata.backend.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryProductListService categoryProductListService;
    private final CategoryService categoryService;
    private final ProductAttributeListService productAttributeListService;
    private final ProductAttributeService productAttributeService;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final ProductAttributeMapper productAttributeMapper;

    public ProductDto findProductDtoById(Integer id) {

        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return null;
        }

        List<ProductAttribute> attributes = productAttributeListService.findByProductId(id);
        CategoryDto category = categoryMapper.toDto(categoryService.findCategoryByProductId(id));

        ProductDto productDto = productMapper.toDto(product);
        productDto.setAttributes(productAttributeMapper.toDtoList(attributes));
        productDto.setCategory(category);

        return productDto;
    }

    public Product findProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    @Transactional
    public ProductDto createProduct(ProductDto productDto) {

        Product createdProduct = productRepository.save(productMapper.toEntity(productDto));

        categoryProductListService.createCategoryProductList(categoryMapper.toEntity(productDto.getCategory()), createdProduct);
        for (ProductAttribute attribute : productAttributeMapper.toEntityList(productDto.getAttributes())) {
            ProductAttribute savedAttribute = productAttributeService.create(attribute);
            productAttributeListService.create(savedAttribute, createdProduct);
        }

        ProductDto createdProductDto = productMapper.toDto(createdProduct);
        createdProductDto.setCategory(categoryMapper.toDto(categoryService.findCategoryByProductId(createdProduct.getId())));
        createdProductDto.setAttributes(productAttributeMapper.toDtoList(productAttributeListService.findByProductId(createdProduct.getId())));

        return createdProductDto;
    }

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public boolean deleteProductById(Integer id) {

        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return false;
        }

        productRepository.delete(product);

        return true;
    }

    public Page<Product> getProductsByCategory(Integer categoryId, Pageable pageable) {
        return productRepository.findAllByCategoryId(categoryId, pageable);
    }

    @Transactional
    public Product updateProduct(Integer id, Product entity) {

        Product existingProduct = productRepository.findById(id).orElse(null);

        if (existingProduct == null) {
            return null;
        }

        entity.setId(id);

        return productRepository.save(entity);
    }
}
