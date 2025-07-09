package com.dialogdata.main.dto;

import com.dialogdata.main.entity.Category;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link Category}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto implements Serializable {

    Integer id;

    @Size(max = 50)
    String name;

    @Size(max = 255)
    String description;
}