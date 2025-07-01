package com.dialogdata.backend.dto;

import com.dialogdata.backend.entity.ProductAttribute;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.dialogdata.backend.entity.ProductAttribute}
 */
@Value
@Builder
public class ProductAttributeDto implements Serializable {

    @NotNull
    @Size(max = 50)
    String name;

    @NotNull
    @Size(max = 255)
    String value;

    public ProductAttribute toEntity() {
        return ProductAttribute.builder()
                .name(this.name)
                .value(this.value)
                .build();
    }
}