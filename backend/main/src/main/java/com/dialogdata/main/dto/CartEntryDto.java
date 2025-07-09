package com.dialogdata.main.dto;

import com.dialogdata.main.entity.CartEntry;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link CartEntry}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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