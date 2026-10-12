package com.klu.ecommerce_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.klu.ecommerce_backend.dto.AuthResponse;
import com.klu.ecommerce_backend.dto.SigninRequest;
import com.klu.ecommerce_backend.dto.SignupRequest;
import com.klu.ecommerce_backend.service.AuthService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestBody SignupRequest request) {

        String response = authService.signup(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(
            @RequestBody SigninRequest request) {

        AuthResponse response = authService.signin(request);

        if (response == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid Email or Password");
        }

        return ResponseEntity.ok(response);
    }
}