package com.dialogdata.main.dto;

import com.dialogdata.main.entity.CartEntry;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link CartEntry}
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