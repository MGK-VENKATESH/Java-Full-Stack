package com.klu.ecommerce_backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendConfirmationEmail(String to, String name) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject("Registration Successful");

        message.setText(
            "Hello " + name + ",\n\n" +
            "Your account has been successfully created.\n\n" +
            "Thank you for registering with our E-Commerce platform."
        );

        mailSender.send(message);
    }
}