package com.tekravio.notification.service.core;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;


@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.expire}")
    private long expireTime;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    public String generateToken(String clientId, String clientSceret, String role) {
        log.info("generateToken user: Roles: {}", role);
        Map<String, Object> claims = new HashMap<>();
        claims.put("clientId", clientId);
        List<String> rolesString = new ArrayList<>();
        rolesString.add(role);
        claims.put("role", rolesString);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("tekravio-notification-service")
                .setSubject(clientId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        Date now = new Date();
        return now.after(expiration);
    }


}
