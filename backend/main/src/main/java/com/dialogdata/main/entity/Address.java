package com.dialogdata.main.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotEmpty
    @Column(name = "street_line", nullable = false, length = 100)
    private String streetLine;

    @Size(max = 20)
    @NotEmpty
    @Column(name = "postal_code", nullable = false, length = 20)
    private String postalCode;

    @Size(max = 50)
    @NotEmpty
    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Size(max = 50)
    @NotEmpty
    @Column(name = "county", nullable = false, length = 50)
    private String county;

    @Size(max = 50)
    @NotEmpty
    @Column(name = "country", nullable = false, length = 50)
    private String country;

}