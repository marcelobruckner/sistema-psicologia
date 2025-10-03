package com.psicologia.service;

import com.psicologia.entity.Usuario;
import com.psicologia.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Tentativa de login para usuário: {}", username);
        
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("Usuário não encontrado no banco: {}", username);
                    return new UsernameNotFoundException("Usuário não encontrado: " + username);
                });

        logger.info("Usuário encontrado: {} - Ativo: {}", usuario.getUsername(), usuario.getAtivo());
        
        if (!usuario.getAtivo()) {
            logger.error("Usuário inativo: {}", username);
            throw new UsernameNotFoundException("Usuário inativo: " + username);
        }

        logger.info("Autenticação bem-sucedida para usuário: {}", username);
        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(new ArrayList<>())
                .build();
    }
}