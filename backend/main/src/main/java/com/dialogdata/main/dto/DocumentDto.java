package com.dialogdata.main.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.dialogdata.main.entity.Document}
 */
@Value
public class DocumentDto implements Serializable {

    Integer id;

    @NotNull
    @Size(max = 255)
    String fileName;

    @NotNull
    @Size(max = 50)
    String contentType;

    @NotNull
    byte[] data;

    @NotNull
    ProductDto product;
}