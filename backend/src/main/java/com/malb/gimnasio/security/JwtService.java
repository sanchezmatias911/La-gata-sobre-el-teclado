
package com.malb.gimnasio.security;

import com.malb.gimnasio.modelo.usuario.Rol;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expiracionMinutos;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiracion-minutos}") long expiracionMinutos
    ) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);

        if (keyBytes.length < 32) {
            throw new IllegalArgumentException(
                    "La clave JWT debe tener al menos 32 bytes"
            );
        }

        if (expiracionMinutos <= 0) {
            throw new IllegalArgumentException(
                    "La expiración del JWT debe ser mayor a cero"
            );
        }

        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.expiracionMinutos = expiracionMinutos;
    }

    public String generarToken(CustomUserDetails usuario) {
        Instant ahora = Instant.now();
        Instant vencimiento = ahora.plusSeconds(getExpiracionSegundos());

        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("id", usuario.getId())
                .claim("rol", usuario.getRol().name())
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(vencimiento))
                .signWith(signingKey)
                .compact();
    }

    public String extraerEmail(String token) {
        return extraerClaims(token).getSubject();
    }

    public Long extraerId(String token) {
        return extraerClaims(token).get("id", Long.class);
    }

    public Rol extraerRol(String token) {
        String rol = extraerClaims(token).get("rol", String.class);
        return Rol.valueOf(rol);
    }

    public boolean esTokenValido(String token, UserDetails usuario) {
        try {
            Claims claims = extraerClaims(token);

            return claims.getSubject().equals(usuario.getUsername())
                    && claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public long getExpiracionSegundos() {
        return expiracionMinutos * 60;
    }

    private Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
