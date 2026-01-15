package com.volunteer.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtProvider {

    private final JwtProperties jwtProperties;
    private final SecretKey key;
    private final Clock clock;

    public JwtProvider(JwtProperties jwtProperties, Clock clock) {
        this.jwtProperties = jwtProperties;
        this.clock = clock;

        byte[] decodedKey = Base64.getDecoder().decode(jwtProperties.getSecret());
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    public String generateToken(Long userId, String email) {
        Date now = Date.from(clock.instant());
        Date expiry = Date.from(clock.instant().plusMillis(jwtProperties.getExpiration()));

        return Jwts.builder()
                .subject(email)
                .claim("id", userId)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    public String getEmail(String token) {
        return getAllClaims(token).getSubject();
    }

    public Long getUserId(String token) {
        return getAllClaims(token).get("id", Long.class);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
