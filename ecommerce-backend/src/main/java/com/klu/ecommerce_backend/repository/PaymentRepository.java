package com.klu.ecommerce_backend.repository;

import com.klu.ecommerce_backend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderOrderId(Long orderId);

    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
}