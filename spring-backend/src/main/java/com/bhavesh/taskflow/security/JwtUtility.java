package com.bhavesh.taskflow.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtility {

    @Value("${JWT_SECRET}")
    private String secret;
    private SecretKey key;
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    @PostConstruct
    private void initKey() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    public String generateToken(String email){
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }


    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token){
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }


    public boolean validateToken(String email, String token, UserDetails userDetails) {
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extractClaims(token).getExpiration().before(new Date());
    }

    
}
