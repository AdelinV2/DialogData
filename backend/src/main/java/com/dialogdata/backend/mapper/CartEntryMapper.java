package com.dialogdata.backend.mapper;

import com.dialogdata.backend.dto.CartEntryDto;
import com.dialogdata.backend.entity.CartEntry;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartEntryMapper {

    CartEntry toEntity(CartEntryDto cartEntryDto);
    CartEntryDto toDto(CartEntry cartEntry);
}
