package com.psicologia.controller;

import com.psicologia.config.JwtUtil;
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

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Login attempt for user: {}", loginRequest.getUsername());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );

            String token = jwtUtil.generateToken(loginRequest.getUsername());
            logger.info("Successful login for user: {}", loginRequest.getUsername());
            
            return ResponseEntity.ok(new LoginResponse(token, loginRequest.getUsername()));
        } catch (BadCredentialsException e) {
            logger.warn("Failed login attempt for user: {} - Invalid credentials", loginRequest.getUsername());
            return ResponseEntity.status(401).build();
        } catch (Exception e) {
            logger.error("Login error for user: {} - {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok(TEST_MESSAGE);
    }
}