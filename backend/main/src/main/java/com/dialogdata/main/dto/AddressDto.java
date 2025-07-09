package com.dialogdata.main.dto;

import com.dialogdata.main.entity.Address;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link Address}
 */
@Value
@Builder
@Data
public class AddressDto implements Serializable {
    
    Integer id;

    @Size(max = 100)
    @NotEmpty
    String streetLine;

    @Size(max = 20)
    @NotEmpty
    String postalCode;

    @Size(max = 50)
    @NotEmpty
    String city;

    @Size(max = 50)
    @NotEmpty
    String county;

    @Size(max = 50)
    @NotEmpty
    String country;


}