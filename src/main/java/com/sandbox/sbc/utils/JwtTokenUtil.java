package com.sandbox.sbc.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    // JWT temporaneo della durata di 15 minuti
    public static final long TEMPORARY_JWT = 900;

    // Il secret del token
    @Value("${jwt.secret}")
    private String secret;

    // Metodo che decodifica il token
    public DecodedJWT decodeJwt(String token) {

        try {
            return JWT.decode(token);
        } catch (JWTDecodeException e) {
            logger.error("Errore nella decodifica del token");
            throw new JWTDecodeException("Errore nella decodifica del token");
        }
    }

    // Restituisce l'username dal token
    public String getUsernameFromToken(String token) {

        DecodedJWT jwt = decodeJwt(token);
        return jwt.getSubject();
    }

    // Restituisce la data di scadenza dal token
    public Date getExpirationDateFromToken(String token) {

        DecodedJWT jwt = decodeJwt(token);
        return jwt.getExpiresAt();
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

        logger.info("Validazione Jwt");
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
