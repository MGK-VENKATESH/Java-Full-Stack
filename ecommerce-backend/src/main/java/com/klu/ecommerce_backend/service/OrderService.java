package com.klu.ecommerce_backend.service;

import com.klu.ecommerce_backend.dto.OrderItemRequest;
import com.klu.ecommerce_backend.dto.OrderRequest;
import com.klu.ecommerce_backend.model.Order;
import com.klu.ecommerce_backend.model.OrderItem;
import com.klu.ecommerce_backend.model.Product;
import com.klu.ecommerce_backend.model.User;
import com.klu.ecommerce_backend.repository.OrderRepository;
import com.klu.ecommerce_backend.repository.ProductRepository;
import com.klu.ecommerce_backend.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Order createOrder(Long userId, OrderRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");

        BigDecimal totalAmount = BigDecimal.ZERO;

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.getItems()) {

            Product product = productRepository
                    .findById(itemRequest.getProductId())
                    .orElseThrow(() ->
                            new RuntimeException("Product not found"));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for " + product.getProductName());
            }

            BigDecimal itemTotal = BigDecimal.valueOf(product.getPrice())
                .multiply(
                        BigDecimal.valueOf(itemRequest.getQuantity())
                );

            totalAmount = totalAmount.add(itemTotal);

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());

            // Price is taken from database
            orderItem.setPrice(BigDecimal.valueOf(product.getPrice()));

            orderItems.add(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setItems(orderItems);

        return orderRepository.save(order);
    }
}