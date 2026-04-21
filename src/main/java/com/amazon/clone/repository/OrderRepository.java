package com.amazon.clone.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.amazon.clone.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByEmailOrderByIdDesc(String email);
}