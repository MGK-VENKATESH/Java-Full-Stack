package com.klu.ecommerce_backend.controller;

import com.klu.ecommerce_backend.dto.OrderRequest;
import com.klu.ecommerce_backend.model.Order;
import com.klu.ecommerce_backend.model.User;
import com.klu.ecommerce_backend.repository.UserRepository;
import com.klu.ecommerce_backend.service.OrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(
            OrderService orderService,
            UserRepository userRepository) {

        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestBody OrderRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Order order = orderService.createOrder(
                user.getUserId(),
                request
        );

        return ResponseEntity.ok(order);
    }
}