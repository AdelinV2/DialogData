package com.dialogdata.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dialogdata.backend.entity.CartEntry}
 */
@Value
public class CartEntryDto implements Serializable {

    @NotNull
    Integer productId;

    @NotNull
    Integer quantity;

    @NotNull
    BigDecimal pricePerPiece;

    @NotNull
    BigDecimal totalPricePerEntry;
}