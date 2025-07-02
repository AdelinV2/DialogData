package com.dialogdata.backend.mapper;

import com.dialogdata.backend.dto.OrderDto;
import com.dialogdata.backend.entity.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toEntity(OrderDto orderDto);

    OrderDto toDto(Order order);

    List<Order> toEntityList(List<OrderDto> orderDtos);

    List<OrderDto> toDtoList(List<Order> orders);

}
