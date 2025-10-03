package com.psicologia.controller;

import com.psicologia.service.JwtService;
import com.psicologia.dto.LoginRequest;
import com.psicologia.dto.LoginResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String TEST_MESSAGE = "Auth endpoint funcionando!";
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    static {
        logger.info("AuthController class loaded");
    }

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authManager, JwtService jwtService) {
        this.authenticationManager = authManager;
        this.jwtService = jwtService;
        logger.info("AuthController initialized successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        String sanitizedUsername = sanitizeForLogging(loginRequest.getUsername());
        logger.info("=== INÍCIO DO LOGIN ===");
        logger.info("Login attempt for user: {}", sanitizedUsername);
        logger.info("Password length: {}", loginRequest.getPassword().length());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );

            String token = jwtService.generateToken((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal());
            logger.info("Successful login for user: {}", sanitizedUsername);
            
            return ResponseEntity.ok(new LoginResponse(token, loginRequest.getUsername()));
        } catch (BadCredentialsException e) {
            logger.error("=== CREDENCIAIS INVÁLIDAS ===");
            logger.error("Failed login attempt for user: {} - Invalid credentials", sanitizedUsername);
            logger.error("Exception message: {}", sanitizeForLogging(e.getMessage()));
            return ResponseEntity.status(401).build();
        } catch (Exception e) {
            logger.error("=== ERRO GERAL NO LOGIN ===");
            logger.error("Login error for user: {} - {}", sanitizedUsername, sanitizeForLogging(e.getMessage()));
            logger.error("Exception type: {}", e.getClass().getSimpleName());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        logger.debug("Test endpoint accessed");
        return ResponseEntity.ok(TEST_MESSAGE);
    }

    private String sanitizeForLogging(String input) {
        if (input == null) {
            return "null";
        }
        return input.replaceAll("[\\r\\n\\t]", "_")
                   .replaceAll("[\\p{Cntrl}]", "")
                   .trim();
    }
}