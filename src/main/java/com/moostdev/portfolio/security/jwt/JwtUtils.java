package com.moostdev.portfolio.security.jwt;

import com.moostdev.portfolio.security.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        try {
            // Verifica se a chave está em formato Base64
            if (jwtSecret.matches("^[A-Za-z0-9+/]+={0,2}$")) {
                return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
            }
            // Se não for Base64, usa a string diretamente
            return Keys.hmacShaKeyFor(jwtSecret.getBytes());
        } catch (Exception e) {
            logger.error("Invalid JWT secret key configuration: {}", e.getMessage());
            throw new IllegalStateException("Invalid JWT secret key configuration", e);
        }
    }

    public String getUserNameFromJwtToken(String token) {
        try {
            // Verifica se o token tem formato válido antes de processar
            if (token == null || token.split("\\.").length != 3) {
                throw new MalformedJwtException("Invalid JWT token structure");
            }

            return Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            logger.error("Cannot extract username from JWT: {}", e.getMessage());
            throw e;
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
            // Verificação adicional do formato do token
            if (authToken == null || authToken.trim().isEmpty()) {
                return false;
            }

            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}