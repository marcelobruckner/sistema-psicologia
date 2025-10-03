package com.psicologia.controller;

import com.psicologia.entity.Usuario;
import com.psicologia.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public TestController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        return usuarioRepository.findByUsername(username)
                .map(user -> ResponseEntity.ok(Map.of(
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "ativo", user.getAtivo(),
                    "passwordHash", user.getPassword().substring(0, 20) + "..."
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/password")
    public ResponseEntity<?> testPassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        
        Usuario user = usuarioRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.ok(Map.of("error", "Usuário não encontrado"));
        }
        
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        
        return ResponseEntity.ok(Map.of(
            "username", username,
            "passwordMatches", matches,
            "inputPassword", password,
            "storedHash", user.getPassword().substring(0, 20) + "..."
        ));
    }

    @PostMapping("/generate-password")
    public ResponseEntity<?> generatePassword(@RequestBody Map<String, String> request) {
        String password = request.get("password");
        String encoded = passwordEncoder.encode(password);
        
        return ResponseEntity.ok(Map.of(
            "plainPassword", password,
            "encodedPassword", encoded
        ));
    }

    @PostMapping("/fix-user-password")
    public ResponseEntity<?> fixUserPassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String newPassword = request.get("password");
        
        Usuario user = usuarioRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.ok(Map.of("error", "Usuário não encontrado"));
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(user);
        
        return ResponseEntity.ok(Map.of(
            "message", "Senha atualizada com sucesso",
            "username", username
        ));
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String email = request.get("email");
        String nomeCompleto = request.get("nomeCompleto");
        
        Usuario user = new Usuario();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setNomeCompleto(nomeCompleto);
        user.setAtivo(true);
        
        usuarioRepository.save(user);
        
        return ResponseEntity.ok(Map.of(
            "message", "Usuário criado com sucesso",
            "username", username
        ));
    }
}