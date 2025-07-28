package com.dialogdata.main.service;

import com.dialogdata.main.client.ImageClient;
import com.dialogdata.main.dto.*;
import com.dialogdata.main.entity.Product;
import com.dialogdata.main.entity.ProductAttribute;
import com.dialogdata.main.entity.ProductAttributeValue;
import com.dialogdata.main.mapper.*;
import com.dialogdata.main.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
@EnableAsync
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
    private final ReviewService reviewService;
    private final DocumentService documentService;
    private final DocumentMapper documentMapper;

    public ProductDto findProductDtoById(Integer id) {

        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return null;
        }

        return this.setDtoFields(product);
    }

    private ProductDto setDtoFields(Product product) {

        ProductDto productDto = productMapper.toDto(product);
        productDto.setCategory(categoryMapper.toDto(categoryService.findCategoryByProductId(product.getId())));
        productDto.setAttributes(productAttributeValueMapper.toDtoList(productAttributeValueService.findByProductId(product.getId())));
        productDto.setAverageRating(reviewService.getAverageRatingByProductId(product.getId()));
        productDto.setImages(getProductImagesByProductId(product.getId()));
        productDto.setDocument(documentService.getDocumentByProductId(product.getId()));

        return productDto;
    }

    public Product findProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }


    @Transactional
    public ProductDto createProduct(ProductDto productDto, MultipartFile file) {

        ProductDto createdProductDto = createProduct(productDto);

        if (file != null) {
            try {
                DocumentDto documentDto = documentService.createDocument(file, productMapper.toEntity(createdProductDto));
                createdProductDto.setDocument(documentDto);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload document: " + e.getMessage(), e);
            }
        }

        return createdProductDto;
    }

    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Product createdProduct = productRepository.save(productMapper.toEntity(productDto));

        for (ProductAttributeValueDto attribute : productDto.getAttributes()) {
            attribute.setProduct(productMapper.toDto(createdProduct));
            ProductAttribute savedAttribute = productAttributeService.create(productAttributeMapper.toEntity(attribute.getAttribute()));
            attribute.setAttribute(productAttributeMapper.toDto(savedAttribute));
            productAttributeValueService.create(attribute);
        }

        for (ImageDto image : productDto.getImages()) {
            image.setFileName(createdProduct.getId() + "_" + image.getFileName());
            imageClient.uploadImage(image);
        }

        return setDtoFields(createdProduct);
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

        imageClient.deleteProductImages(id);

        return true;
    }

    public Page<ProductDto> getProductsByCategory(Integer categoryId, Pageable pageable) {

        Page<Product> products = productRepository.findAllByCategoryId(categoryId, pageable);

        return products.map(this::setDtoFields);
    }

    @Transactional
    public ProductDto updateProduct(Integer id, ProductDto productDto, MultipartFile file) {

        ProductDto updatedProduct = updateProduct(id, productDto);

        if (file != null) {
            try {
                documentService.deleteDocumentByProductId(productDto.getId());
                DocumentDto documentDto = documentService.createDocument(file, productMapper.toEntity(updatedProduct));
                updatedProduct.setDocument(documentDto);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload document: " + e.getMessage(), e);
            }
        }

        return setDtoFields(productRepository.findById(id).orElse(null));
    }

    @Transactional
    public ProductDto updateProduct(Integer id, ProductDto product) {

        Product existingProduct = productRepository.findById(id).orElse(null);

        if (existingProduct == null) {
            return null;
        }

        product.setId(id);
        imageClient.deleteProductImages(id);

        Product savedProduct = productRepository.save(productMapper.toEntity(product));

        for (ImageDto image : product.getImages()) {
            image.setFileName(savedProduct.getId() + "_" + image.getFileName());
            imageClient.uploadImage(image);
        }

        return setDtoFields(savedProduct);
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

    public Page<ProductDto> getProductsByCategoryIdAndSearchAndAttribute(Pageable pageable, Integer categoryId, String search, List<String> attributeValue) {

        Page<Product> products = productRepository
                .findAllByCategoryIdAndNameContainingIgnoreCaseAndAttributeValue(categoryId, search, attributeValue, pageable);

        return products.map(this::setDtoFields);
    }

    @Async
    public CompletableFuture<CsvDto> addProductsFromCsv(MultipartFile file) {

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

        return CompletableFuture.completedFuture(csvDto);
    }


    public List<ProductDto> getPromotedProducts() {

        List<Product> promotedProductsEntities = productRepository.findAllByPromoted(true);
        List<ProductDto> promotedProducts = promotedProductsEntities.stream()
                .map(this::setDtoFields)
                .toList();

        return promotedProducts;
    }
}
