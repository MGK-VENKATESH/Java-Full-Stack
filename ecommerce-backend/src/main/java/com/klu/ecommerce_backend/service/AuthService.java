package com.klu.ecommerce_backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.klu.ecommerce_backend.security.JwtService;
import com.klu.ecommerce_backend.dto.AuthResponse;
import com.klu.ecommerce_backend.dto.SigninRequest;
import com.klu.ecommerce_backend.dto.SignupRequest;
import com.klu.ecommerce_backend.model.Role;
import com.klu.ecommerce_backend.model.User;
import com.klu.ecommerce_backend.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            EmailService emailService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public String signup(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already registered";
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPassword(
            passwordEncoder.encode(request.getPassword())
        );

        if (request.getRole() == null) {
            user.setRole(Role.USER);
        } else {
            user.setRole(request.getRole());
        }

        // Save user to database
        userRepository.save(user);

        // Send confirmation email after successful registration
        emailService.sendConfirmationEmail(
                user.getEmail(),
                user.getName()
        );

        return "User registered successfully";
    }

    public AuthResponse signin(SigninRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return null;
        }

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {
            return null;
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                "Sign In Successful",
                token
        );
    }
}