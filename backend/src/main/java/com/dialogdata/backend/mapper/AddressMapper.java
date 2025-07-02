package com.dialogdata.backend.mapper;

import com.dialogdata.backend.dto.AddressDto;
import com.dialogdata.backend.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toEntity(AddressDto addressDto);
    AddressDto toDto(Address address);
}
