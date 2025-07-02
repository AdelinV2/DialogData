package com.dialogdata.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link com.dialogdata.backend.entity.CartEntry}
 */
@Value
@Data
public class CartEntryDto implements Serializable {

    Integer id;

    @NotNull
    Integer productId;

    @NotNull
    Integer quantity;

    @NotNull
    BigDecimal pricePerPiece;

    @NotNull
    BigDecimal totalPricePerEntry;
}