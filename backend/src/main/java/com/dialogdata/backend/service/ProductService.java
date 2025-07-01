package com.dialogdata.backend.service;

import com.dialogdata.backend.entity.Product;
import com.dialogdata.backend.repository.ProductRepository;
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

    public Product createProduct(Product product) {

        return productRepository.save(product);
    }

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
