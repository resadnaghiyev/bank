package com.rashad.bank.api.auth.service;

import com.rashad.bank.api.auth.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${jwt.access-token-expiration-time-ms}")
    private long JWT_ACCESS_TOKEN_VALIDITY; // 30 deq

    @Value("${jwt.refresh-token-expiration-time-ms}")
    private long JWT_REFRESH_TOKEN_VALIDITY; // 1 saat

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String generateAccessToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getId());
        return createAccessToken(claims, user.getPin());
    }

    private String createAccessToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuer("bank")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_ACCESS_TOKEN_VALIDITY))
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getId());
        return createRefreshToken(claims, user.getPin());
    }

    private String createRefreshToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuer("bank")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY))
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key getSignKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
