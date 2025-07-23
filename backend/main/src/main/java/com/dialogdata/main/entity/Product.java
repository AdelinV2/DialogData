package com.dialogdata.main.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 50)
    @NotEmpty
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Size(max = 255)
    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "price", nullable = false, precision = 7, scale = 2)
    private BigDecimal price;

    @NotNull
    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    @NotNull
    @ColumnDefault("CURRENT_DATE")
    @Column(name = "added_date", nullable = false)
    private LocalDate addedDate;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "promoted", nullable = false)
    private Boolean promoted = false;

    @ColumnDefault("NULL")
    @Column(name = "promotion_price", precision = 7, scale = 2)
    private BigDecimal promotionPrice;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
}