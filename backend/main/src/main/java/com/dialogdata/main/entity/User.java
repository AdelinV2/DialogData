package com.dialogdata.main.entity;

import com.dialogdata.main.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotEmpty
    @Column(name = "first_name", nullable = false, length = 50)
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @NotEmpty
    @Column(name = "last_name", nullable = false, length = 50)
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @Size(max = 255)
    @NotEmpty
    @Column(name = "email", nullable = false)
    @Email
    private String email;

    @NotEmpty
    @Column(name = "phone_number", nullable = false, length = 20)
    @Pattern(regexp = "^[0-9]+$", message = "Phone number must contain only digits")
    private String phoneNumber;

    @NotEmpty
    @Column(name = "password", nullable = false, length = 60)
    @Size(max = 60, message = "Password must not exceed 60 characters")
    private String password;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "delivery_address_id", nullable = false)
    private Address deliveryAddress;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "billing_address_id", nullable = false)
    private Address billingAddress;

    @NotNull
    @Column(name = "subscribed", nullable = false)
    @ColumnDefault(value = "false")
    private Boolean subscribed = false;

    @NotNull
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    @ColumnDefault(value = "0")
    private Role role = Role.USER;

}