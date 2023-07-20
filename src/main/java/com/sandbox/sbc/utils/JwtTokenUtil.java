package com.sandbox.sbc.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    // JWT temporaneo della durata di 15 minuti
    public static final long TEMPORARY_JWT = 900;

    // Il secret del token
    @Value("${jwt.secret}")
    private String secret;

    // Metodo che restituisce i claims dal token
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    // Restituisce l'username dal token
    public String getUsernameFromToken(String token) {

        return getClaim(token, Claims::getSubject);
    }

    // Restituisce la data di scadenza dal token
    public Date getExpirationDateFromToken(String token) {

        return getClaim(token, Claims::getExpiration);
    }

    // Controlla se è scaduto il token
    private Boolean isTokenExpired(String token) {

        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Genera un token
    public String generateToken(UserDetails userDetails) {

        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TEMPORARY_JWT * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    // Metodo di validazione del token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
