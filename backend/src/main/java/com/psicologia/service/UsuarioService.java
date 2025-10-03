package com.psicologia.service;

import com.psicologia.dto.UsuarioRequest;
import com.psicologia.entity.Usuario;
import com.psicologia.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<Usuario> listarComPaginacao(Pageable pageable) {
        return usuarioRepository.findByAtivoTrue(pageable);
    }

    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarioRepository.findById(id);
    }

    public Usuario salvar(UsuarioRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username já existe");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já existe");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setEmail(request.getEmail());
        usuario.setNomeCompleto(request.getNomeCompleto());
        usuario.setRole(request.getRole());
        
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(UUID id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!request.getUsername().equals(usuario.getUsername()) 
            && usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username já existe");
        }
        
        if (!request.getEmail().equals(usuario.getEmail()) 
            && usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já existe");
        }

        usuario.setUsername(request.getUsername());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        usuario.setEmail(request.getEmail());
        usuario.setNomeCompleto(request.getNomeCompleto());
        usuario.setRole(request.getRole());
        
        return usuarioRepository.save(usuario);
    }

    public void excluir(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }
}