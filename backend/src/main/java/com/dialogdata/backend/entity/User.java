package com.dialogdata.backend.entity;

import com.dialogdata.backend.dto.UserDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
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

    @Size(max = 50)
    @NotEmpty
    @Column(name = "first_name", nullable = false, length = 50)
    @Max(value = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @Size(max = 50)
    @NotEmpty
    @Column(name = "last_name", nullable = false, length = 50)
    @Max(value = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @Size(max = 255)
    @NotEmpty
    @Column(name = "email", nullable = false)
    @Email
    private String email;

    @Size(max = 20)
    @NotEmpty
    @Column(name = "phone_number", nullable = false, length = 20)
    @Pattern(regexp = "^[0-9]+$", message = "Phone number must contain only digits")
    private String phoneNumber;

    @Size(max = 50)
    @NotEmpty
    @Column(name = "password", nullable = false, length = 50)
    @Max(value = 50, message = "Password must not exceed 50 characters")
    private String password;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "delivery_address_id", nullable = false)
    private Address deliveryAddress;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "billing_address_id", nullable = false)
    private Address billingAddress;

    public UserDto toDto() {
        return UserDto.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .deliveryAddress(this.deliveryAddress.toDto())
                .billingAddress(this.billingAddress.toDto())
                .build();
    }
}