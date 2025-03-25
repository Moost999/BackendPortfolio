package com.moostdev.portfolio.controllers;

import com.moostdev.portfolio.dto.LoginRequest;
import com.moostdev.portfolio.dto.SignupRequest;
import com.moostdev.portfolio.dto.JwtResponse;
import com.moostdev.portfolio.dto.MessageResponse;
import com.moostdev.portfolio.security.jwt.JwtUtils;
import com.moostdev.portfolio.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            String jwt = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            String jwt = authService.registerUser(signUpRequest);
            return ResponseEntity.ok(new JwtResponse(jwt)); // Retorna o JWT após registro
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}