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

    Integer id;

    @NotNull
    @Size(max = 50)
    String name;

    @NotNull
    @Size(max = 255)
    String value;

}