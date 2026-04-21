package com.amazon.clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.amazon.clone.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}