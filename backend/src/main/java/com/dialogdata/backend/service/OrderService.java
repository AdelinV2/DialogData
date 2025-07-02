package com.dialogdata.backend.service;

import com.dialogdata.backend.entity.Order;
import com.dialogdata.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order create(Order order) {
        return orderRepository.save(order);
    }

    public Order findById(Integer id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> findAllByUserId(Integer userId) {
        return orderRepository.findAllByUserId(userId);
    }
}
