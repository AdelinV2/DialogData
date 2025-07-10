package com.dialogdata.main.mapper;

import com.dialogdata.main.dto.CartDto;
import com.dialogdata.main.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

//    @Mapping(source = "user", target = "user.id")
    Cart toEntity(CartDto cartDto);

//    @Mapping(source = "user.id", target = "userId")
    CartDto toDto(Cart cart);
}
