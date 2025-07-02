package com.dialogdata.backend.dto;

import com.dialogdata.backend.entity.User;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.dialogdata.backend.entity.User}
 */
@Value
@Builder
@Data
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

}