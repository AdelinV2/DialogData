package com.dialogdata.main.controller;

import com.dialogdata.main.dto.OrderDto;
import com.dialogdata.main.entity.Order;
import com.dialogdata.main.mapper.OrderMapper;
import com.dialogdata.main.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Operation(summary = "Get all orders of a user")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    @ApiResponse(responseCode = "204", description = "No orders found for the user")
    @GetMapping("/user/{id}")
    public ResponseEntity<Page<OrderDto>> getAllOrdersOfUser(@Parameter(description = "ID of the user", required = true)
                                                             @PathVariable("id") Integer userId,
                                                             Pageable pageable) {

        Page<Order> orders = orderService.getAllOrdersOfUser(userId, pageable);

        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(orders.map(orderMapper::toDto));
    }

    @Operation(summary = "Get all orders ordered by date")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
    @ApiResponse(responseCode = "204", description = "No orders found")
    @GetMapping
    public ResponseEntity<Page<OrderDto>> getAllOrdersByDate(Pageable pageable) {

        Page<Order> orders = orderService.getAllOrdersOrderedByDate(pageable);

        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(orders.map(orderMapper::toDto));
    }

    @Operation(summary = "Get order by ID")
    @ApiResponse(responseCode = "200", description = "Order found")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@Parameter(description = "ID of the order", required = true)
                                                 @PathVariable("id") Integer id) {

        OrderDto orderDto = orderService.getOrderDtoById(id);

        if (orderDto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(orderDto);
    }

    @Operation(summary = "Create a new order")
    @ApiResponse(responseCode = "201", description = "Order created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid order data")
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Parameter(description = "Order to be created", required = true)
                                                @RequestBody OrderDto orderDto) {

        Order createdOrder = orderService.create(orderMapper.toEntity(orderDto));

        if (createdOrder == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(201).body(orderMapper.toDto(createdOrder));
    }
}
