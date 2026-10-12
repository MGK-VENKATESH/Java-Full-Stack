package com.klu.ecommerce_backend.repository;

import com.klu.ecommerce_backend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}