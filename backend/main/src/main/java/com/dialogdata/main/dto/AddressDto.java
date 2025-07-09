package com.dialogdata.main.dto;

import com.dialogdata.main.entity.Address;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO for {@link Address}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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