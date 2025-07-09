package com.dialogdata.main.mapper;

import com.dialogdata.main.dto.CartEntryDto;
import com.dialogdata.main.entity.CartEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartEntryMapper {

    @Mapping(source = "productId", target = "product.id")
    CartEntry toEntity(CartEntryDto cartEntryDto);

    @Mapping(source = "product.id", target = "productId")
    CartEntryDto toDto(CartEntry cartEntry);

    List<CartEntry> toEntityList(List<CartEntryDto> cartEntryDtos);

    List<CartEntryDto> toDtoList(List<CartEntry> cartEntries);
}
