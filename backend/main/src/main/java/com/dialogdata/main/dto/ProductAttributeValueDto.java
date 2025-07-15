package com.dialogdata.main.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link com.dialogdata.main.entity.ProductAttributeValue}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeValueDto implements Serializable {

    Integer id;

    @NotNull
    ProductDto product;

    @NotNull
    ProductAttributeDto attribute;

    @NotNull
    @Size(max = 255)
    String value;
}