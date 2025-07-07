package com.dialogdata.backend.dto;

import com.dialogdata.backend.entity.Order;
import com.dialogdata.backend.util.PaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO for {@link Order}
 */
@Value
public class OrderDto implements Serializable {

    Integer id;

    @NotNull
    Integer userId;

    @NotNull
    CartDto cart;

    @NotNull
    PaymentType paymentType;

    @NotNull
    AddressDto deliveryAddress;

    @NotNull
    AddressDto invoiceAddress;

    @NotNull
    BigDecimal totalPrice;

    @NotNull
    LocalDate orderDate;
}