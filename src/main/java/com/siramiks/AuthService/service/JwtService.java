package com.siramiks.AuthService.service;

import com.siramiks.AuthService.model.AuthenticatedUserDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("${JWT_HELPER_SECRET}")
  public String SECRET;

  public void validateToken(final String token) {
    Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
  }


  public String generateToken(AuthenticatedUserDTO user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userRole", user.getRole());
    claims.put("userId", user.getId());
    return createToken(claims, user.getEmail());
  }

  private String createToken(Map<String, Object> claims, String userName) {
    return Jwts.builder()
            .setClaims(claims)
            .setSubject(userName)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30mins
            .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
  }

  private Key getSignKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
