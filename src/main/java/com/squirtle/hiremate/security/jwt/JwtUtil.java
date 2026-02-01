package com.squirtle.hiremate.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtUtil {

    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    private Key getSignedKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user){
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*2))
                .signWith(getSignedKey())
                .compact();
    }

    private Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmail(String token){
        return getClaims(token).getSubject();
    }

    private boolean isExpired(String token) {
        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }

    public boolean verifyToken(String token, String email){
        try{
            return getClaims(token).getSubject().equals(email) && isExpired(token);
        }catch (JwtException _){
            return false;
        }
    }
}
