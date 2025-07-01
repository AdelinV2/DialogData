package com.dialogdata.backend.dto;

import com.dialogdata.backend.entity.Cart;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dialogdata.backend.entity.Cart}
 */
@Value
@Builder
@Data
public class CartDto implements Serializable {

    @NotNull
    Integer userId;

    @NotNull
    BigDecimal totalPrice;
}