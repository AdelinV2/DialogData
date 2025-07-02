package com.dialogdata.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link com.dialogdata.backend.entity.Cart}
 */
@Data
public class CartDto implements Serializable {

    Integer id;

    @NotNull
    Integer userId;

    @NotNull
    BigDecimal totalPrice;

    List<CartEntryDto> cartEntries;
}