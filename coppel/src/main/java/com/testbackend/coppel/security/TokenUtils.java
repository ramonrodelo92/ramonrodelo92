package com.testbackend.coppel.security;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class TokenUtils {
    public static String SECRET_KEY = "supersecretkey534535$&/()qwdjsaioflkfknsd$%/(%)$%&/(())2345";
    public static Long TOKEN_VALIDITY_SECONDS = 2_592_000L;

    public static String generateToken(String email) {
        Map<String, Object> extras = new HashMap<>();
        extras.put("email", email);

        String token = Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY_SECONDS * 1_000L))
                .addClaims(extras)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();

        return token;
    }

    public static UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes()).build().parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();

            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        } catch (JwtException e) {
            return null;
        }

    }
}
