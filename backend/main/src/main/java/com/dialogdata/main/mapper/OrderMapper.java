package com.dialogdata.main.mapper;

import com.dialogdata.main.dto.OrderDto;
import com.dialogdata.main.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toEntity(OrderDto orderDto);

    OrderDto toDto(Order order);

    List<Order> toEntityList(List<OrderDto> orderDtos);

    List<OrderDto> toDtoList(List<Order> orders);

}
