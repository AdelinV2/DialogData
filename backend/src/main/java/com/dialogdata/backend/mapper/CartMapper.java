package com.dialogdata.backend.mapper;

import com.dialogdata.backend.dto.CartDto;
import com.dialogdata.backend.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {

    Cart toEntity(CartDto cartDto);
    CartDto toDto(Cart cart);
}
