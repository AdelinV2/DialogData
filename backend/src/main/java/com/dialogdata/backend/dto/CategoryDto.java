package com.dialogdata.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.dialogdata.backend.entity.Category}
 */
@Value
public class CategoryDto implements Serializable {

    Integer id;

    @Size(max = 50)
    @NotEmpty
    String name;

    @Size(max = 255)
    String description;
}