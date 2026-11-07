package com.malb.gimnasio;

import com.malb.gimnasio.dao.UsuarioDAO;
import com.malb.gimnasio.modelo.usuario.Administrador;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.modelo.usuario.Usuario;

import com.malb.gimnasio.security.CustomUserDetails;
import com.malb.gimnasio.security.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class JwtAuthFilterIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private JwtService jwtService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Test
    void sinToken_endpointProtegido_devuelve401() throws Exception {
        mockMvc.perform(get("/profesores"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void tokenValidoDeCliente_permiteConsultarSuPerfil() throws Exception {
        Cliente cliente = crearCliente();
        String token = generarToken(cliente);

        mockMvc.perform(get("/clientes/{id}", cliente.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void tokenValidoDeAdmin_permiteConsultarProfesores() throws Exception {
        crearCliente();
        Administrador admin = crearAdministrador();
        String token = generarToken(admin);

        mockMvc.perform(get("/profesores")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void tokenValidoDeCliente_noPermiteConsultarProfesores() throws Exception {
        Cliente cliente = crearCliente();
        String token = generarToken(cliente);

        mockMvc.perform(get("/profesores")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void tokenValidoDeCliente_noPermiteConsultarPerfilAjeno() throws Exception {
        Cliente cliente = crearCliente();
        Cliente otroCliente = crearOtroCliente();

        String token = generarToken(cliente);

        mockMvc.perform(get("/clientes/{id}", otroCliente.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void tokenVencido_devuelve401() throws Exception {
        Cliente cliente = crearCliente();
        String token = generarTokenVencido(cliente);

        mockMvc.perform(get("/profesores")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void tokenConFirmaIncorrecta_devuelve401() throws Exception {
        Cliente cliente = crearCliente();

        String token = Jwts.builder()
                .subject(cliente.getEmail())
                .claim("id", cliente.getId())
                .claim("rol", "CLIENTE")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(3600)))
                .signWith(Keys.hmacShaKeyFor(
                        "otra-clave-secreta-de-prueba-de-al-menos-32-bytes"
                                .getBytes(StandardCharsets.UTF_8)))
                .compact();

        mockMvc.perform(get("/profesores")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void tokenDeUsuarioInexistente_devuelve401() throws Exception {
        String token = Jwts.builder()
                .subject("inexistente@test.com")
                .claim("id", 999999L)
                .claim("rol", "CLIENTE")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(3600)))
                .signWith(Keys.hmacShaKeyFor(
                        jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .compact();

        mockMvc.perform(get("/profesores")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void endpointPublico_permiteAccesoSinToken() throws Exception {
        mockMvc.perform(get("/actividades"))
                .andExpect(status().isOk());
    }

    // ---------------------------------------------------------
    // AUXILIARES
    // ---------------------------------------------------------

    private Cliente crearCliente() {
        Cliente cliente = new Cliente(
                40111222L,
                "cliente.jwt@test.com",
                "password123",
                "Cliente JWT",
                "1144556677"
        );

        return (Cliente) usuarioDAO.save(cliente);
    }

    private Cliente crearOtroCliente() {
        Cliente cliente = new Cliente(
                40333444L,
                "otro.jwt@test.com",
                "password123",
                "Otro Cliente",
                "1144556678"
        );

        return (Cliente) usuarioDAO.save(cliente);
    }

    private Administrador crearAdministrador() {
        Administrador admin = new Administrador(
                30111222L,
                "admin.jwt@test.com",
                "password-hasheada-de-prueba",
                "Admin JWT",
                "1144556679"
        );

        return (Administrador) usuarioDAO.save(admin);
    }

    private String generarToken(Usuario usuario) {
        return jwtService.generarToken(new CustomUserDetails(usuario));
    }

    private String generarTokenVencido(Cliente cliente) {
        Instant ahora = Instant.now();

        return Jwts.builder()
                .subject(cliente.getEmail())
                .claim("id", cliente.getId())
                .claim("rol", "CLIENTE")
                .issuedAt(Date.from(ahora.minusSeconds(120)))
                .expiration(Date.from(ahora.minusSeconds(60)))
                .signWith(Keys.hmacShaKeyFor(
                        jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}