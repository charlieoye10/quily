package com.example.quily.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

   @Value("${jwt.secret}")
   private String secretKey;

   @Value("${jwt.expiration}")
   private Long expirationTime;

   public String generateToken(String userEmail) {
      return Jwts.builder()
         .setSubject(userEmail)
         .setIssuedAt(new Date(System.currentTimeMillis()))
         .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
         .signWith(getSigningKey())
         .compact();
   }

   public Boolean validateToken(String token, String username) {
      final String usernameFromToken = getEmailFromToken(token);
      boolean b = (usernameFromToken.equals(username) && !isTokenExpired(token));
      return b;
   }

   public String getEmailFromToken(String token) {
      return getClaimFromToken(token, Claims::getSubject);
   }

   public Date getExpirationDateFromToken(String token) {
      return getClaimFromToken(token, Claims::getExpiration);
   }

   public Boolean isTokenExpired(String token) {
      final Date expiration = getExpirationDateFromToken(token);
      return expiration.before(new Date());
   }

   public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
      final Claims claims = getAllClaimsFromToken(token);
      return claimsResolver.apply(claims);
   }

   private Claims getAllClaimsFromToken(String token) {
      return Jwts.parserBuilder()
         .setSigningKey(getSigningKey())
         .build()
         .parseClaimsJws(token)
         .getBody();
   }

   private SecretKey getSigningKey() {
      byte[] decodedKey = Base64.getDecoder().decode(secretKey);
      return Keys.hmacShaKeyFor(decodedKey);
   }
}

