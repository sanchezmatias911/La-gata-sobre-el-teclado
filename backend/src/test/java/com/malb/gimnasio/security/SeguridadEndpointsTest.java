package com.malb.gimnasio.security;

import com.malb.gimnasio.dao.ClienteDAO;
import com.malb.gimnasio.modelo.usuario.Rol;
import com.malb.gimnasio.service.LimpiezaBaseDeDatosService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// ============================================================================
// Verifica el "Contrato propuesto": qué es público, qué pide token (CLIENTE o
// ADMIN), qué es solo ADMIN y qué mensajes devuelven los 401.
// Usa la SecurityConfig real (no mocks) y la base H2 del perfil "test".
// ============================================================================

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SeguridadEndpointsTest {

    private static final String MSG_SIN_TOKEN = "Necesitás iniciar sesión para acceder a este recurso.";
    private static final String MSG_VENCIDO = "Tu sesión venció. Iniciá sesión nuevamente.";
    private static final String MSG_INVALIDO = "Token inválido.";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteDAO clienteDAO;

    @Autowired
    private LimpiezaBaseDeDatosService limpiezaBaseDeDatosService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @AfterEach
    void limpiar() {
        limpiezaBaseDeDatosService.limpiarBaseDeDatos();
    }

    // ------------------------------------------------------------------------
    // PÚBLICO, SIN TOKEN
    // ------------------------------------------------------------------------

    @ParameterizedTest(name = "{0} {1} es público")
    @CsvSource({
            "GET,/clases",
            "GET,/actividades"
    })
    void publico_sinToken_devuelve200(String metodo, String ruta) throws Exception {
        mockMvc.perform(request(HttpMethod.valueOf(metodo), ruta))
                .andExpect(status().isOk());
    }

    @ParameterizedTest(name = "POST {0} es público (llega a la validación)")
    @CsvSource({
            "/auth/login",
            "/auth/registro"
    })
    void publico_login_y_registro_noPidenToken(String ruta) throws Exception {
        mockMvc.perform(post(ruta).contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ------------------------------------------------------------------------
    // SIN TOKEN EN UN ENDPOINT PROTEGIDO -> 401
    // ------------------------------------------------------------------------

    @ParameterizedTest(name = "sin token: {0} {1} -> 401")
    @CsvSource({
            "POST,/clases",
            "DELETE,/clases/1",
            "POST,/actividades",
            "DELETE,/actividades/1",
            "POST,/profesores",
            "GET,/profesores",
            "GET,/profesores/1",
            "DELETE,/profesores/1",
            "GET,/clientes",
            "GET,/clientes/1",
            "POST,/clientes",
            "DELETE,/clientes/1",
            "POST,/clases/1/suscripcion",
            "DELETE,/clases/1/desuscribir/1",
            "GET,/actuator/metrics",
            "GET,/endpoint-nuevo"
    })
    void sinToken_endpointProtegido_devuelve401(String metodo, String ruta) throws Exception {
        mockMvc.perform(pedido(metodo, ruta))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value(MSG_SIN_TOKEN));
    }

    // ------------------------------------------------------------------------
    // CLIENTE: lo exclusivo de ADMIN le devuelve 403
    // ------------------------------------------------------------------------

    @ParameterizedTest(name = "CLIENTE: {0} {1} -> 403")
    @CsvSource({
            "POST,/clases",
            "DELETE,/clases/1",
            "POST,/actividades",
            "DELETE,/actividades/1",
            "POST,/profesores",
            "GET,/profesores",
            "GET,/profesores/1",
            "DELETE,/profesores/1",
            "GET,/clientes",
            "POST,/clientes",
            "DELETE,/clientes/1",
            "GET,/actuator/metrics",
            "GET,/endpoint-nuevo"
    })
    @WithUsuarioAutenticado(rol = Rol.CLIENTE)
    void cliente_endpointExclusivoDeAdmin_devuelve403(String metodo, String ruta) throws Exception {
        mockMvc.perform(pedido(metodo, ruta))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    // ------------------------------------------------------------------------
    // CLIENTE: "solo sobre sí mismo" (el usuario simulado tiene id = 1)
    // ------------------------------------------------------------------------

    @Test
    @WithUsuarioAutenticado(id = 1L, rol = Rol.CLIENTE)
    void cliente_noPuedeVerOtroCliente_devuelve403() throws Exception {
        mockMvc.perform(get("/clientes/2"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUsuarioAutenticado(id = 987654L, rol = Rol.CLIENTE)
    void cliente_puedeVerElSuyo_pasaLaSeguridad() throws Exception {
        // Pasa seguridad y control de propiedad; no existe en la base -> 404 (no 403).
        mockMvc.perform(get("/clientes/987654"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUsuarioAutenticado(id = 1L, rol = Rol.CLIENTE)
    void cliente_noPuedeSuscribirAOtroCliente_devuelve403() throws Exception {
        mockMvc.perform(post("/clases/1/suscripcion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idCliente\": 2}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUsuarioAutenticado(id = 1L, rol = Rol.CLIENTE)
    void cliente_noPuedeDesuscribirAOtroCliente_devuelve403() throws Exception {
        mockMvc.perform(request(HttpMethod.DELETE, "/clases/1/desuscribir/2"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUsuarioAutenticado(id = 987654L, rol = Rol.CLIENTE)
    void cliente_puedeSuscribirseASiMismo_pasaLaSeguridad() throws Exception {
        // La clase no existe -> 404 (no 403): pasó la seguridad y el control de propiedad.
        mockMvc.perform(post("/clases/999999/suscripcion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idCliente\": 987654}"))
                .andExpect(status().isNotFound());
    }

    // ------------------------------------------------------------------------
    // ADMIN
    // ------------------------------------------------------------------------

    @ParameterizedTest(name = "ADMIN: GET {0} -> 200")
    @CsvSource({
            "/profesores",
            "/clientes"
    })
    @WithUsuarioAutenticado(rol = Rol.ADMIN)
    void admin_puedeListar(String ruta) throws Exception {
        mockMvc.perform(get(ruta)).andExpect(status().isOk());
    }

    @Test
    @WithUsuarioAutenticado(id = 99L, rol = Rol.ADMIN)
    void admin_puedeVerCualquierCliente_pasaLaSeguridad() throws Exception {
        mockMvc.perform(get("/clientes/987654")).andExpect(status().isNotFound());
    }

    // ------------------------------------------------------------------------
    // TOKEN VENCIDO / INVÁLIDO -> 401 con mensajes distintos
    // ------------------------------------------------------------------------

    @Test
    void tokenInvalido_devuelve401() throws Exception {
        mockMvc.perform(get("/profesores").header("Authorization", "Bearer esto.no.es.un.jwt"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(MSG_INVALIDO));
    }

    @Test
    void tokenConFirmaIncorrecta_devuelve401() throws Exception {
        String token = token("otra-clave-distinta-de-al-menos-32-bytes-xxxxxxxx",
                Instant.now(), Instant.now().plusSeconds(3600));

        mockMvc.perform(get("/profesores").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(MSG_INVALIDO));
    }

    @Test
    void tokenVencido_devuelve401() throws Exception {
        String token = token(jwtSecret,
                Instant.now().minusSeconds(7200), Instant.now().minusSeconds(3600));

        mockMvc.perform(get("/profesores").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(MSG_VENCIDO));
    }

    @Test
    void tokenVencido_enEndpointPublico_noImpide_elAcceso() throws Exception {
        String token = token(jwtSecret,
                Instant.now().minusSeconds(7200), Instant.now().minusSeconds(3600));

        mockMvc.perform(get("/clases").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    // ------------------------------------------------------------------------
    // LOGIN y CONTRASEÑAS ENCRIPTADAS
    // ------------------------------------------------------------------------

    @Test
    void login_conCredencialesIncorrectas_devuelve401ConMensajeDelContrato() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"no@existe.com\", \"password\": \"cualquiera1\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Mail o contraseña incorrectos."));
    }

    @Test
    @WithUsuarioAutenticado(rol = Rol.ADMIN)
    void clienteCreadoPorAdmin_seGuardaEncriptado_yPuedeIniciarSesion() throws Exception {
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dni": 45123456,
                                  "email": "login.test@gimnasio.com",
                                  "password": "password123",
                                  "nombre": "Cliente Login",
                                  "telefono": "1112345678"
                                }
                                """))
                .andExpect(status().isOk());

        String guardada = clienteDAO.findByDni(45123456L).orElseThrow().getPassword();
        assertNotEquals("password123", guardada);
        assertTrue(guardada.startsWith("$2"), "La password debería estar hasheada con BCrypt");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"login.test@gimnasio.com\", \"password\": \"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.vencimiento").isNumber())
                .andExpect(jsonPath("$.usuario.rol").value("CLIENTE"))
                .andExpect(jsonPath("$.usuario.email").value("login.test@gimnasio.com"));
    }

    @Test
    @WithUsuarioAutenticado(rol = Rol.ADMIN)
    void crearCliente_conPasswordCorta_devuelve400() throws Exception {
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dni": 45123457,
                                  "email": "corta@gimnasio.com",
                                  "password": "1234",
                                  "nombre": "Cliente Corto",
                                  "telefono": "1112345678"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    // ------------------------------------------------------------------------
    // Auxiliares
    // ------------------------------------------------------------------------

    private org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder pedido(String metodo, String ruta) {
        return request(HttpMethod.valueOf(metodo), ruta)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}");
    }

    private String token(String secret, Instant emitido, Instant vence) {
        return Jwts.builder()
                .subject("alguien@test.com")
                .issuedAt(Date.from(emitido))
                .expiration(Date.from(vence))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
