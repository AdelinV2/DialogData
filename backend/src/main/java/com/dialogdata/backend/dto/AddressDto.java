package com.dialogdata.backend.dto;

import com.dialogdata.backend.entity.Address;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.dialogdata.backend.entity.Address}
 */
@Value
@Builder
@Data
public class AddressDto implements Serializable {

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

    public Address toEntity() {
        return Address.builder()
                .streetLine(streetLine)
                .postalCode(postalCode)
                .city(city)
                .county(county)
                .country(country)
                .build();
    }
}