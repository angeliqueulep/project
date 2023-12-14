package com.humber.project.service;

import com.humber.project.model.MenuItem;
import com.humber.project.model.Order;
import com.humber.project.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findByStatusIsNotNull();
    }

    public Order getOrderById(Integer id) {
        return orderRepository.findById(id).get();
    }
}
