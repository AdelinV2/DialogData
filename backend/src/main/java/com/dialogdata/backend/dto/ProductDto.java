package com.dialogdata.backend.dto;

import com.dialogdata.backend.entity.Category;
import com.dialogdata.backend.entity.Product;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link com.dialogdata.backend.entity.Product}
 */
@Value
@Builder
public class ProductDto implements Serializable {

    Integer id;

    @Size(max = 50)
    @NotEmpty
    String name;

    @Size(max = 255)
    @NotEmpty
    String description;

    @NotNull
    BigDecimal price;

    @NotNull
    Integer availableQuantity;

    LocalDate addedDate;

    List<ProductAttributeDto> attributes;

    Category category;
}