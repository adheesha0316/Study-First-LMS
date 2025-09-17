package com.campus.lms.utill;

import com.campus.lms.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JWTTokenGenerator {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:3600000}") // 1 hour default (in ms)
    private long expiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /** Generate token for a given user */
    public String generateToken(User user) {
        return Jwts.builder()
                .id(String.valueOf(user.getUserId()))
                .subject(user.getEmail())
                .claim("username", user.getUsername())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    /** Validate token */
    public boolean validateToken(String token) {
        try {
            getAllClaims(token); // if parsing fails, it's invalid
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /** Extract email (subject) */
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** Extract username */
    public String extractUsername(String token) {
        return getAllClaims(token).get("username", String.class);
    }

    /** Extract role */
    public String extractUserRole(String token) {
        return getAllClaims(token).get("role", String.class);
    }

    /** Extract expiration date */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // ---------------------- PRIVATE HELPERS ---------------------- //

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
