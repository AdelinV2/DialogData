package com.dialogdata.main.service;

import com.dialogdata.main.dto.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvService {

    @Transactional
    public ProductDto addProductFromCsv(String line) {
        String[] values = line.split(",");

        if (values.length < 7) {
            System.err.println("Invalid CSV line: " + line);
            throw new IllegalArgumentException("Invalid CSV line: " + line);
        }

        CategoryDto category = new CategoryDto();
        category.setId(Integer.valueOf(values[4]));

        ProductDto product = ProductDto.builder()
                .name(values[0])
                .description(values[1])
                .price(new BigDecimal(values[2]))
                .availableQuantity(Integer.parseInt(values[3]))
                .category(category)
                .addedDate(LocalDate.now())
                .build();


        String[] attributeValuesList = values[5].split(";");

        List<ProductAttributeValueDto> productAttributeValues = new ArrayList<>();

        for (String attributeValueList : attributeValuesList) {
            String[] attributeValues = attributeValueList.split("/");

            ProductAttributeValueDto productAttributeValueDto = new ProductAttributeValueDto();
            ProductAttributeDto productAttributeDto = new ProductAttributeDto();

            productAttributeValueDto.setProduct(product);
            productAttributeValueDto.setValue(attributeValues[1]);
            productAttributeDto.setName(attributeValues[0]);
            productAttributeValueDto.setAttribute(productAttributeDto);
            productAttributeValues.add(productAttributeValueDto);
        }

        product.setAttributes(productAttributeValues);

        String[] imageUrls = values[6].split(";");

        List<ImageDto> images = new ArrayList<>();

        for (int i = 0; i < imageUrls.length; i++) {
            String imageUrl = imageUrls[i];
            ImageDto imageDto = new ImageDto();
            imageDto.setBase64(downloadImage(imageUrl));
            imageDto.setFileName(String.valueOf(i));
            images.add(imageDto);
        }

        product.setImages(images);

        return product;
    }

    private String downloadImage(String imageUrl) {
        try (InputStream in = URI.create(imageUrl).toURL().openStream()) {
            byte[] bytes = in.readAllBytes();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            System.err.println("Error downloading image from URL: " + imageUrl);
            throw new RuntimeException(e);
        }
    }
}