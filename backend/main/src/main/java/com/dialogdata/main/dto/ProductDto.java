package com.dialogdata.main.dto;

import com.dialogdata.main.entity.Product;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link Product}
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
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

    List<ProductAttributeValueDto> attributes;

    CategoryDto category;

    List<ImageDto> images;

    Boolean promoted;

    BigDecimal promotionPrice;

    BigDecimal averageRating;
}