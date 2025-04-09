package com.example.todolistspring.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {
    private final String SECRET_KEY = "mWQKjKflpJSqyj0nDdSG9ZHE6x4tNaXGb35J6d7G5mo=";
    private final long EXPIRATION_TIME = 86400000; // 24h

    public String generateToken(String username) {
        return Jwts.builder().
                header().
                keyId(SECRET_KEY).
                and().
                subject(username).
                expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)).
                signWith( generalKey() ).
                compact();
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public boolean validateToken(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith( generalKey() )
                    .build();
            parser.parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private SecretKey generalKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
