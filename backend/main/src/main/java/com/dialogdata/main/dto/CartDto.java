package com.dialogdata.main.dto;

import com.dialogdata.main.entity.Cart;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for {@link Cart}
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