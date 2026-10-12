package com.corhuila.edutrack.academic.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collections;

@Component
public class JwtProvider {

    private final SecretKey key;

    public JwtProvider(
            @Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            java.util.UUID.fromString(getClaims(token).get("userId", String.class));
            return true;
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger(JwtProvider.class).error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String username = claims.get("userId", String.class);
        java.util.UUID.fromString(username);
        String role = claims.get("role", String.class);
        
        return new UsernamePasswordAuthenticationToken(
                username, 
                null, 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}
