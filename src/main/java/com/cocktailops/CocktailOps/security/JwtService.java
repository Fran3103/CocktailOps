package com.cocktailops.CocktailOps.security;

import com.cocktailops.CocktailOps.entitie.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

     @Value("${security.jwt.expiration-minutes}")
    private long expirationMinutes;


     public String generateToken(User user) {

         Instant now = Instant.now();

         return Jwts.builder()
                 .subject(user.getEmail())
                 .claim("userId", user.getId())
                 .claim("role", user.getRole())
                 .issuedAt(Date.from(now))
                 .expiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
                 .signWith(getSigningKey())
                 .compact();
     }

     public String extractUsername(String token) {
         return extractAllClaims(token).getSubject();
     }

     public boolean isTokenValid(String token, UserDetails userDetails){
         String username = extractUsername(token);

         return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
     }

     private boolean isTokenExpired(String token){
         return extractAllClaims(token)
                 .getExpiration()
                 .before(new Date());
     }

     private Claims extractAllClaims(String token){

         return Jwts.parser()
                 .verifyWith(getSigningKey())
                 .build()
                 .parseSignedClaims(token)
                 .getPayload();
     }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
