package com.aashray.hiremate.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "AA69AD3CB8FD6B618DB971E8DDD5DAssiTussiAlimightyPussyAA69AD3CB8FD6B618DB971E8DDD5D";

    private Key getSignedKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(getSignedKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean verifyToken(String token){
        try{
            getClaims(token);
            return true;
        }catch (JwtException _){
            return false;
        }
    }


    public String getEmail(String token){
        return getClaims(token).getSubject();
    }
}