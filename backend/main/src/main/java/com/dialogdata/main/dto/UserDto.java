package com.dialogdata.main.dto;

import com.dialogdata.main.entity.User;
import com.dialogdata.main.enums.Role;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {

    Integer id;

    @Size(max = 50)
    @NotEmpty
    String firstName;

    @Size(max = 50)
    @NotEmpty
    String lastName;

    @Size(max = 255)
    @Email
    @NotEmpty

    String email;
    @Size(max = 20)
    @Pattern(message = "Phone number must contain only digits", regexp = "^[0-9]+$")
    @NotEmpty
    String phoneNumber;

    @Size(max = 60)
    @NotEmpty
    String password;

    @NotNull
    AddressDto deliveryAddress;

    @NotNull
    AddressDto billingAddress;

    Boolean subscribed;

    Role role;

}