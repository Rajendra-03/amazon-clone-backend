package com.amazon.clone.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.amazon.clone.model.Order;
import com.amazon.clone.repository.OrderRepository;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderRepository.save(order);
    }

    @GetMapping("/history/{email}")
    public List<Order> getOrderHistory(@PathVariable String email) {
        return orderRepository.findByEmailOrderByIdDesc(email);
    }
}