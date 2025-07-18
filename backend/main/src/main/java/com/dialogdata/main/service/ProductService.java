package com.dialogdata.main.service;

import com.dialogdata.main.client.ImageClient;
import com.dialogdata.main.dto.*;
import com.dialogdata.main.entity.Product;
import com.dialogdata.main.entity.ProductAttribute;
import com.dialogdata.main.entity.ProductAttributeValue;
import com.dialogdata.main.mapper.CategoryMapper;
import com.dialogdata.main.mapper.ProductAttributeMapper;
import com.dialogdata.main.mapper.ProductAttributeValueMapper;
import com.dialogdata.main.mapper.ProductMapper;
import com.dialogdata.main.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryProductListService categoryProductListService;
    private final CategoryService categoryService;
    private final ProductAttributeValueService productAttributeValueService;
    private final ProductAttributeService productAttributeService;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final ProductAttributeMapper productAttributeMapper;
    private final ProductAttributeValueMapper productAttributeValueMapper;
    private final ImageClient imageClient;
    private final CsvService csvService;

    public ProductDto findProductDtoById(Integer id) {

        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return null;
        }

        List<ProductAttributeValue> attributes = productAttributeValueService.findByProductId(id);
        CategoryDto category = categoryMapper.toDto(categoryService.findCategoryByProductId(id));

        ProductDto productDto = productMapper.toDto(product);
        productDto.setAttributes(productAttributeValueMapper.toDtoList(attributes));
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
        for (ProductAttributeValueDto attribute : productDto.getAttributes()) {
            attribute.setProduct(productMapper.toDto(createdProduct));
            ProductAttribute savedAttribute = productAttributeService.create(productAttributeMapper.toEntity(attribute.getAttribute()));
            attribute.setAttribute(productAttributeMapper.toDto(savedAttribute));
            productAttributeValueService.create(attribute);
        }

        ProductDto createdProductDto = productMapper.toDto(createdProduct);
        createdProductDto.setCategory(categoryMapper.toDto(categoryService.findCategoryByProductId(createdProduct.getId())));
        createdProductDto.setAttributes(productAttributeValueMapper.toDtoList(productAttributeValueService.findByProductId(createdProduct.getId())));

        for (ImageDto image : productDto.getImages()) {
            image.setFileName(createdProductDto.getId() + "_" + image.getFileName());
            imageClient.uploadImage(image);
        }

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

    public List<ImageDto> getProductImagesByProductId(Integer productId) {

        List<String> imageUrls = imageClient.getProductImagesUrl(productId);

        return imageUrls.stream()
                .map(url -> {
                    ImageDto dto = new ImageDto();
                    dto.setImageUrl(url);
                    return dto;
                })
                .toList();
    }

    public void updateProductStock(Integer id, int quantity) {

        Product product = productRepository.findById(id).orElse(null);

        if (product != null) {
            int newQuantity = product.getAvailableQuantity() + quantity;
            product.setAvailableQuantity(newQuantity);
            productRepository.save(product);
        } else {
            throw new IllegalArgumentException("Product not found with ID: " + id);
        }
    }

    public Page<Product> getProductsByCategoryIdAndSearchAndAttribute(Pageable pageable, Integer categoryId, String search, List<String> attributeValue) {

        return productRepository
                .findAllByCategoryIdAndNameContainingIgnoreCaseAndAttributeValue(categoryId, search, attributeValue, pageable);
    }

    public CsvDto addProductsFromCsv(MultipartFile file) {

        CsvDto csvDto = new CsvDto(0, new HashSet<>());
        int index = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            String line;

            for (int i = 1; (line = br.readLine()) != null; i++) {
                index = i;

                try {
                    ProductDto product = csvService.addProductFromCsv(line);
                    if (this.createProduct(product) != null) {
                        csvDto.setSuccessCount(csvDto.getSuccessCount() + 1);
                    } else {
                        csvDto.addFailedLine(index);
                    }
                    ;
                } catch (Exception e) {
                    csvDto.addFailedLine(index);
                }

            }

        } catch (Exception e) {
            csvDto.addFailedLine(index);
        }

        return csvDto;
    }


}
