
package com.malb.gimnasio.service;

import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.modelo.usuario.Rol;
import com.malb.gimnasio.security.CustomUserDetails;
import com.malb.gimnasio.security.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET =
            "clave-secreta-de-prueba-con-mas-de-32-bytes";

    private JwtService jwtService;
    private CustomUserDetails usuario;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(SECRET, 120);

        Cliente cliente = new Cliente(
                40111222L,
                "ana@mail.com",
                "$2a$12$hashDePrueba",
                "Ana Perez",
                "1156789012"
        );

        // Simulamos el ID que asignaría la base de datos al persistirlo.
        ReflectionTestUtils.setField(cliente, "id", 1L);

        usuario = new CustomUserDetails(cliente);
    }

    @Test
    void generarTokenDevuelveUnTokenNoVacio() {
        String token = jwtService.generarToken(usuario);

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void generarTokenIncluyeEmailIdYRol() {
        String token = jwtService.generarToken(usuario);

        assertEquals("ana@mail.com", jwtService.extraerEmail(token));
        assertEquals(1, jwtService.extraerId(token));
        assertEquals(Rol.CLIENTE, jwtService.extraerRol(token));
    }

    @Test
    void tokenGeneradoEsValidoParaSuUsuario() {
        String token = jwtService.generarToken(usuario);

        assertTrue(jwtService.esTokenValido(token, usuario));
    }

    @Test
    void tokenNoEsValidoParaOtroUsuario() {
        String token = jwtService.generarToken(usuario);

        Cliente otroCliente = new Cliente(
                40111223L,
                "otro@mail.com",
                "$2a$12$hashDePrueba",
                "Otro Cliente",
                "1156789013"
        );

        CustomUserDetails otroUsuario =
                new CustomUserDetails(otroCliente);

        assertFalse(jwtService.esTokenValido(token, otroUsuario));
    }

    @Test
    void tokenConFirmaIncorrectaNoEsValido() {
        String token = jwtService.generarToken(usuario);

        JwtService otroJwtService = new JwtService(
                "otra-clave-secreta-de-prueba-con-mas-de-32-bytes",
                120
        );

        assertFalse(otroJwtService.esTokenValido(token, usuario));
    }

    @Test
    void tokenVencidoNoEsValido() {
        SecretKey key = Keys.hmacShaKeyFor(
                SECRET.getBytes(StandardCharsets.UTF_8)
        );

        Instant ahora = Instant.now();

        String tokenVencido = Jwts.builder()
                .subject(usuario.getEmail())
                .claim("id", usuario.getId())
                .claim("rol", usuario.getRol().name())
                .issuedAt(Date.from(ahora.minusSeconds(120)))
                .expiration(Date.from(ahora.minusSeconds(60)))
                .signWith(key)
                .compact();

        assertFalse(jwtService.esTokenValido(tokenVencido, usuario));
    }

    @Test
    void expiracionSeDevuelveEnSegundos() {
        assertEquals(7200, jwtService.getExpiracionSegundos());
    }
}
