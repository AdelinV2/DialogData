package com.dialogdata.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginDto {

    Integer id;

    @NotEmpty
    String email;

    @NotEmpty
    String password;
}
