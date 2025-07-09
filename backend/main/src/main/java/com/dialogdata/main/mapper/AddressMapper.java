package com.dialogdata.main.mapper;

import com.dialogdata.main.dto.AddressDto;
import com.dialogdata.main.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toEntity(AddressDto addressDto);
    AddressDto toDto(Address address);
}
