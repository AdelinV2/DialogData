package com.dialogdata.main.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.dialogdata.main.entity.Review}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto implements Serializable {

    Integer id;

    @NotNull
    ProductDto product;

    @NotNull
    UserDto user;

    @NotNull
    Integer rating;

    @Size(max = 255)
    String comment;

    LocalDate createdDate;

    Boolean verified;
}