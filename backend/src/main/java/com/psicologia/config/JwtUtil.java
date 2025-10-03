package com.psicologia.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.JwtException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${spring.security.jwt.secret}")
    private String secret;

    @Value("${spring.security.jwt.expiration}")
    private Long expiration;

    private Key signingKey;
    private JwtParser jwtParser;

    @PostConstruct
    private void init() {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build();
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    private Claims getClaimsFromToken(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token, String username) {
        try {
            String tokenUsername = getUsernameFromToken(token);
            return (tokenUsername.equals(username) && !isTokenExpired(token));
        } catch (JwtException e) {
            logger.debug("JWT validation failed: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error during JWT validation: {}", e.getMessage());
            return false;
        }
    }
}