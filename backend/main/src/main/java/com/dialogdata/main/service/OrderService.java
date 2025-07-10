package com.dialogdata.main.service;

import com.dialogdata.main.dto.CartEntryDto;
import com.dialogdata.main.dto.OrderDto;
import com.dialogdata.main.entity.Cart;
import com.dialogdata.main.entity.Order;
import com.dialogdata.main.mapper.OrderMapper;
import com.dialogdata.main.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserService userService;
    private final EmailService emailService;
    private final ProductService productService;
    private final OrderMapper orderMapper;

    @Transactional
    public Order create(Order order) {

        Cart cart = cartService.findById(order.getCart().getId());

        if (cart == null) {
            return null;
        }

        cart.setActive(false);
        cartService.create(cart);
        cartService.createEmptyCart(userService.getUserById(order.getUserId()));


        order.getCart().getCartEntries().forEach(entry -> {
            entry.setProduct(productService.findProductById(entry.getProduct().getId()));
            productService.updateProductStock(entry.getProduct().getId(), -entry.getQuantity());
        });

        Order createdOrder = orderRepository.save(order);
        emailService.sendOrderEmail(userService.getUserById(order.getUserId()).getEmail(), createdOrder);

        return createdOrder;
    }

    public Order findById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Page<Order> getAllOrdersOfUser(Integer userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }

    public OrderDto getOrderDtoById(Integer id) {

        Order order = orderRepository.findById(id).orElse(null);

        if (order == null) {
            return null;
        }

        OrderDto orderDto = orderMapper.toDto(order);

        for (CartEntryDto entry : orderDto.getCart().getCartEntries()) {
            entry.getProduct().setImages(productService.getProductImagesByProductId(entry.getProduct().getId()));
        }

        return orderDto;
    }
}
