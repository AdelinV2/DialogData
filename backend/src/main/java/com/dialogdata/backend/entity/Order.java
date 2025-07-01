package com.dialogdata.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Address cart;


    // TODO add enum for payment type
    @NotNull
    @Column(name = "payment_type", nullable = false)
    private Integer paymentType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_address_id", nullable = false)
    private Address deliveryAddress;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "invoice_address_id", nullable = false)
    private Address invoiceAddressId;

    @NotNull
    @Column(name = "total_price", nullable = false, precision = 7, scale = 2)
    private BigDecimal totalPrice;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

}