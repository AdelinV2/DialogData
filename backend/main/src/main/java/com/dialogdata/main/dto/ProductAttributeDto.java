package com.dialogdata.main.dto;

import com.dialogdata.main.entity.ProductAttribute;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link ProductAttribute}
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
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