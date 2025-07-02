package com.dialogdata.backend.mapper;

import com.dialogdata.backend.dto.CartDto;
import com.dialogdata.backend.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "userId", target = "user.id")
    Cart toEntity(CartDto cartDto);

    @Mapping(source = "user.id", target = "userId")
    CartDto toDto(Cart cart);
}
