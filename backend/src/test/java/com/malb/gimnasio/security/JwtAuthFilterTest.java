package com.malb.gimnasio.security;

import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.modelo.usuario.Rol;
import com.malb.gimnasio.security.error.MotivoRechazoJwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtAuthFilterTest {

    private static final String SECRETO = "clave-secreta-de-prueba-con-mas-de-32-bytes";
    private static final String HASH_DE_PRUEBA = "$2a$12$hashDePrueba";
    private static final String EMAIL = "ana@mail.com";

    private final JwtService jwtService = new JwtService(SECRETO, 60);
    private final CustomUserDetailsService userDetailsService = mock(CustomUserDetailsService.class);
    private final JwtAuthFilter filtro = new JwtAuthFilter(jwtService, userDetailsService);

    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();
    private final MockFilterChain cadena = new MockFilterChain();

    private CustomUserDetails ana;

    @BeforeEach
    void crearUsuario() {
        ana = usuarioConEmail(EMAIL, 1L);
    }

    @AfterEach
    void limpiarContextoDeSeguridad() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void sinEncabezadoAuthorization_noAutentica_yContinuaLaCadena() throws Exception {
        filtro.doFilter(request, response, cadena);

        assertNull(autenticacionActual());
        assertEquals(Optional.empty(), MotivoRechazoJwt.registradoEn(request));
        assertNotNull(cadena.getRequest());
    }

    @Test
    void encabezadoQueNoEsBearer_seIgnora() throws Exception {
        request.addHeader(HttpHeaders.AUTHORIZATION, "Basic abc123");

        filtro.doFilter(request, response, cadena);

        assertNull(autenticacionActual());
        assertEquals(Optional.empty(), MotivoRechazoJwt.registradoEn(request));
    }

    @Test
    void tokenValido_autenticaConIdentidadYRolDeLaBaseDeDatos() throws Exception {
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(ana);
        enviarToken(jwtService.generarToken(ana));

        filtro.doFilter(request, response, cadena);

        Authentication autenticacion = autenticacionActual();
        assertEquals(new UsuarioAutenticado(1L, EMAIL, Rol.CLIENTE), autenticacion.getPrincipal());
        assertEquals(List.of("ROLE_CLIENTE"), autoridadesDe(autenticacion));
        assertNotNull(cadena.getRequest());
    }

    @Test
    void tokenVencido_noAutentica_yRegistraMotivoVencido() throws Exception {
        enviarToken(tokenFirmadoCon(SECRETO, Instant.now().minusSeconds(120), Instant.now().minusSeconds(60)));

        filtro.doFilter(request, response, cadena);

        assertNull(autenticacionActual());
        assertEquals(Optional.of(MotivoRechazoJwt.VENCIDO), MotivoRechazoJwt.registradoEn(request));
        assertNotNull(cadena.getRequest());
    }

    @Test
    void tokenFirmadoConOtraClave_noAutentica_yRegistraMotivoInvalido() throws Exception {
        enviarToken(tokenFirmadoCon(
                "otra-clave-secreta-de-prueba-con-mas-de-32-bytes",
                Instant.now(),
                Instant.now().plusSeconds(3600)
        ));

        filtro.doFilter(request, response, cadena);

        assertNull(autenticacionActual());
        assertEquals(Optional.of(MotivoRechazoJwt.INVALIDO), MotivoRechazoJwt.registradoEn(request));
    }

    @Test
    void tokenDeUsuarioQueYaNoExiste_noAutentica_yRegistraMotivoInvalido() throws Exception {
        when(userDetailsService.loadUserByUsername(EMAIL)).thenThrow(new UsernameNotFoundException("no existe"));
        enviarToken(jwtService.generarToken(ana));

        filtro.doFilter(request, response, cadena);

        assertNull(autenticacionActual());
        assertEquals(Optional.of(MotivoRechazoJwt.INVALIDO), MotivoRechazoJwt.registradoEn(request));
    }

    @Test
    void tokenQueNoCorrespondeAlUsuarioCargado_noAutentica_yRegistraMotivoInvalido() throws Exception {
        when(userDetailsService.loadUserByUsername(EMAIL)).thenReturn(usuarioConEmail("otro@mail.com", 2L));
        enviarToken(jwtService.generarToken(ana));

        filtro.doFilter(request, response, cadena);

        assertNull(autenticacionActual());
        assertEquals(Optional.of(MotivoRechazoJwt.INVALIDO), MotivoRechazoJwt.registradoEn(request));
    }

    private void enviarToken(String token) {
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }

    private Authentication autenticacionActual() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private List<String> autoridadesDe(Authentication autenticacion) {
        return autenticacion.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }

    private CustomUserDetails usuarioConEmail(String email, long id) {
        Cliente cliente = new Cliente(40111000L + id, email, HASH_DE_PRUEBA, "Cliente de Prueba", "1156789012");
        ReflectionTestUtils.setField(cliente, "id", id);
        return new CustomUserDetails(cliente);
    }

    private String tokenFirmadoCon(String secreto, Instant emitidoEn, Instant venceEn) {
        return Jwts.builder()
                .subject(EMAIL)
                .claim("id", 1L)
                .claim("rol", Rol.CLIENTE.name())
                .issuedAt(Date.from(emitidoEn))
                .expiration(Date.from(venceEn))
                .signWith(Keys.hmacShaKeyFor(secreto.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
