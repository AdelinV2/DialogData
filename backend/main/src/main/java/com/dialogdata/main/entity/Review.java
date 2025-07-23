package com.dialogdata.main.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Size(max = 255)
    @Column(name = "comment")
    private String comment;

    @NotNull
    @ColumnDefault("CURRENT_DATE")
    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "verified", nullable = false)
    private Boolean verified = false;

}