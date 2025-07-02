package com.dialogdata.backend.service;

import com.dialogdata.backend.entity.Cart;
import com.dialogdata.backend.entity.Order;
import com.dialogdata.backend.repository.OrderRepository;
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

    @Transactional
    public Order create(Order order) {

        Cart cart = cartService.findById(order.getCart().getId());

        if (cart == null) {
            return null;
        }

        cart.setActive(false);
        cartService.create(cart);
        cartService.createEmptyCart(userService.getUserById(order.getUserId()));

        return orderRepository.save(order);
    }

    public Order findById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    public Page<Order> getAllOrdersOfUser(Integer userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }
}
