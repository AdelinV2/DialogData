package com.dialogdata.backend.service;

import com.dialogdata.backend.entity.Product;
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

    public Product findById(Integer id) {

        return productRepository.findById(id).orElse(null);
    }

    @Transactional
    public Product createProduct(Product product) {

        return productRepository.save(product);
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
