package com.malb.gimnasio;

import com.jayway.jsonpath.JsonPath;
import com.malb.gimnasio.dao.UsuarioDAO;
import com.malb.gimnasio.modelo.usuario.Administrador;
import com.malb.gimnasio.security.JwtAuthFilter;
import com.malb.gimnasio.security.WithUsuarioAutenticado;
import com.malb.gimnasio.service.LimpiezaBaseDeDatosService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.filter.CorsFilter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Complementa a SeguridadEndpointsTest: ese verifica QUÉ rol puede entrar a
// cada ruta; este verifica CÓMO está armada la seguridad:
//   - beans (PasswordEncoder BCrypt costo 12, AuthenticationManager)
//   - cadena de filtros (JwtAuthFilter bien ubicado; CSRF/Basic/formLogin off)
//   - sesión STATELESS (nunca crea sesión ni cookie)
//   - CORS (orígenes, métodos y encabezados permitidos / rechazados)
//   - encabezados de seguridad por defecto
//   - formato JSON de los errores 401/403
//   - rutas públicas de infraestructura (health, swagger)
//   - flujo completo: login -> token -> endpoint de ADMIN (crear profesor)

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = "cors.allowed-origins=http://localhost:5173")
class ConfiguracionSeguridadTest {

    private static final String ORIGEN_PERMITIDO = "http://localhost:5173";
    private static final String ORIGEN_AJENO = "https://sitio-malicioso.com";

    private static final String MSG_SIN_TOKEN = "Necesitás iniciar sesión para acceder a este recurso.";
    private static final String MSG_CREDENCIALES = "Mail o contraseña incorrectos.";

    private static final String EMAIL_ADMIN = "admin.config.seguridad@gimnasio.com";
    private static final String PASSWORD_ADMIN = "AdminSeguro123";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private FilterChainProxy filterChainProxy;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private LimpiezaBaseDeDatosService limpiezaBaseDeDatosService;

    @AfterEach
    void limpiar() {
        limpiezaBaseDeDatosService.limpiarBaseDeDatos();
        // La limpieza general no borra administradores: se elimina solo el de este test.
        usuarioDAO.findByEmailIgnoreCase(EMAIL_ADMIN).ifPresent(usuarioDAO::delete);
    }

    // ------------------------------------------------------------------------
    // BEANS
    // ------------------------------------------------------------------------

    @Test
    void passwordEncoder_esBCryptConCosto12() {
        assertInstanceOf(BCryptPasswordEncoder.class, passwordEncoder);

        String hash = passwordEncoder.encode("password123");

        assertTrue(hash.startsWith("$2"), "Debería ser un hash BCrypt");
        assertTrue(hash.contains("$12$"), "El costo de BCrypt debería ser 12");
    }

    @Test
    void passwordEncoder_noGuardaTextoPlano_yUsaSalAleatoria() {
        String hash1 = passwordEncoder.encode("password123");
        String hash2 = passwordEncoder.encode("password123");

        assertNotEquals("password123", hash1);
        assertNotEquals(hash1, hash2, "Con sal aleatoria, dos hashes de la misma clave difieren");
        assertTrue(passwordEncoder.matches("password123", hash1));
        assertTrue(passwordEncoder.matches("password123", hash2));
        assertFalse(passwordEncoder.matches("otraClave123", hash1));
    }

    @Test
    void authenticationManager_estaDisponible() {
        assertNotNull(authenticationManager);
    }

    // ------------------------------------------------------------------------
    // CADENA DE FILTROS
    // ------------------------------------------------------------------------

    @Test
    void cadenaDeFiltros_incluyeJwt_antesDeLaAutorizacion_yDespuesDeCors() {
        List<Class<?>> filtros = clasesDeLosFiltros();

        int cors = filtros.indexOf(CorsFilter.class);
        int jwt = filtros.indexOf(JwtAuthFilter.class);
        int autorizacion = filtros.indexOf(AuthorizationFilter.class);

        assertTrue(cors >= 0, "Falta CorsFilter (cors habilitado)");
        assertTrue(jwt >= 0, "Falta JwtAuthFilter en la cadena");
        assertTrue(autorizacion >= 0, "Falta AuthorizationFilter");
        assertTrue(cors < jwt, "CORS debe ejecutarse antes que el filtro JWT (los preflight no llevan token)");
        assertTrue(jwt < autorizacion, "El JWT debe autenticar ANTES de evaluar la autorización");
    }

    @Test
    void cadenaDeFiltros_noTieneCsrfNiBasicNiFormLogin() {
        List<Class<?>> filtros = clasesDeLosFiltros();

        assertFalse(filtros.contains(CsrfFilter.class), "CSRF debería estar deshabilitado (API stateless con JWT)");
        assertFalse(filtros.contains(BasicAuthenticationFilter.class), "HTTP Basic debería estar deshabilitado");
        assertFalse(filtros.contains(UsernamePasswordAuthenticationFilter.class), "formLogin debería estar deshabilitado");
    }

    // ------------------------------------------------------------------------
    // STATELESS, CSRF, BASIC Y FORM LOGIN
    // ------------------------------------------------------------------------

    @Test
    @WithUsuarioAutenticado
    void sesion_esStateless_noCreaSesionNiCookie() throws Exception {
        MvcResult resultado = mockMvc.perform(get("/profesores"))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist(HttpHeaders.SET_COOKIE))
                .andReturn();

        assertNull(resultado.getRequest().getSession(false), "No debería haberse creado una HttpSession");
    }

    @Test
    @WithUsuarioAutenticado
    void csrf_deshabilitado_unPostSinTokenCsrfNoEsRechazadoPor403() throws Exception {
        // Si CSRF estuviera activo, sería 403 antes de llegar a la validación del DTO.
        mockMvc.perform(post("/profesores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void httpBasic_deshabilitado_credencialesBasicSeIgnoran() throws Exception {
        String basic = Base64.getEncoder().encodeToString(
                (EMAIL_ADMIN + ":" + PASSWORD_ADMIN).getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(get("/profesores").header(HttpHeaders.AUTHORIZATION, "Basic " + basic))
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist(HttpHeaders.WWW_AUTHENTICATE))
                .andExpect(jsonPath("$.message").value(MSG_SIN_TOKEN));
    }

    @Test
    void formLogin_deshabilitado_noHayPaginaDeLoginNiRedireccion() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist(HttpHeaders.LOCATION));

        mockMvc.perform(post("/login")
                        .param("username", EMAIL_ADMIN)
                        .param("password", PASSWORD_ADMIN))
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist(HttpHeaders.LOCATION));
    }

    // ------------------------------------------------------------------------
    // CORS
    // ------------------------------------------------------------------------

    @Test
    void cors_preflightDesdeOrigenPermitido_sinToken_esAceptado() throws Exception {
        mockMvc.perform(options("/profesores")
                        .header(HttpHeaders.ORIGIN, ORIGEN_PERMITIDO)
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "authorization,content-type"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ORIGEN_PERMITIDO))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, containsString("POST")))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, containsStringIgnoringCase("authorization")))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600"));
    }

    @Test
    void cors_preflightPermiteLosMetodosDelContrato() throws Exception {
        for (String metodo : List.of("GET", "POST", "PUT", "DELETE")) {
            mockMvc.perform(options("/clases")
                            .header(HttpHeaders.ORIGIN, ORIGEN_PERMITIDO)
                            .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, metodo))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void cors_preflightDesdeOrigenAjeno_esRechazado() throws Exception {
        mockMvc.perform(options("/profesores")
                        .header(HttpHeaders.ORIGIN, ORIGEN_AJENO)
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    @Test
    void cors_preflightConMetodoNoPermitido_esRechazado() throws Exception {
        mockMvc.perform(options("/clases")
                        .header(HttpHeaders.ORIGIN, ORIGEN_PERMITIDO)
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PATCH"))
                .andExpect(status().isForbidden());
    }

    @Test
    void cors_preflightConEncabezadoNoPermitido_esRechazado() throws Exception {
        mockMvc.perform(options("/clases")
                        .header(HttpHeaders.ORIGIN, ORIGEN_PERMITIDO)
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "x-encabezado-raro"))
                .andExpect(status().isForbidden());
    }

    @Test
    void cors_peticionRealDesdeOrigenPermitido_incluyeElEncabezado() throws Exception {
        mockMvc.perform(get("/clases").header(HttpHeaders.ORIGIN, ORIGEN_PERMITIDO))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ORIGEN_PERMITIDO));
    }

    @Test
    void cors_peticionRealDesdeOrigenAjeno_esRechazada() throws Exception {
        mockMvc.perform(get("/clases").header(HttpHeaders.ORIGIN, ORIGEN_AJENO))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    @Test
    void cors_noPermiteCredenciales_niComodinDeOrigen() throws Exception {
        mockMvc.perform(get("/clases").header(HttpHeaders.ORIGIN, ORIGEN_PERMITIDO))
                .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ORIGEN_PERMITIDO));
    }

    // ------------------------------------------------------------------------
    // ENCABEZADOS DE SEGURIDAD Y FORMATO DE ERRORES
    // ------------------------------------------------------------------------

    @Test
    void encabezadosDeSeguridadPorDefecto_estanPresentes() throws Exception {
        mockMvc.perform(get("/clases"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(header().string("X-Frame-Options", "DENY"));
    }

    @Test
    void error401_esJsonUtf8_conEstructuraApiError() throws Exception {
        mockMvc.perform(get("/profesores"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().encoding(StandardCharsets.UTF_8))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value(MSG_SIN_TOKEN));
    }

    @Test
    @WithUsuarioAutenticado(rol = com.malb.gimnasio.modelo.usuario.Rol.CLIENTE)
    void error403_esJsonUtf8_conEstructuraApiError() throws Exception {
        mockMvc.perform(get("/profesores"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().encoding(StandardCharsets.UTF_8))
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    // ------------------------------------------------------------------------
    // RUTAS PÚBLICAS DE INFRAESTRUCTURA
    // ------------------------------------------------------------------------

    @Test
    void actuatorHealth_esPublico() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    void documentacionSwagger_esPublica() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());

        MvcResult ui = mockMvc.perform(get("/swagger-ui.html")).andReturn();
        int estado = ui.getResponse().getStatus();
        assertTrue(estado != 401 && estado != 403,
                "Swagger UI no debería exigir autenticación (estado " + estado + ")");
    }

    // ------------------------------------------------------------------------
    // FLUJO COMPLETO: LOGIN -> TOKEN -> ENDPOINT DE ADMIN
    // ------------------------------------------------------------------------

    @Test
    void admin_inicialPuedeIniciarSesion_yCrearUnProfesorConSuToken() throws Exception {
        crearAdministrador();
        String token = iniciarSesion(EMAIL_ADMIN, PASSWORD_ADMIN);

        mockMvc.perform(post("/profesores")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dni": 30111222,
                                  "email": "profe.config@gimnasio.com",
                                  "nombre": "Profe Config",
                                  "telefono": "1144556677",
                                  "biografia": "Profesora de yoga",
                                  "especialidades": "Yoga"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Profe Config"));

        mockMvc.perform(get("/profesores").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("profe.config@gimnasio.com"));
    }

    @Test
    void cliente_registradoEIniciadoSesion_noPuedeCrearProfesores() throws Exception {
        mockMvc.perform(post("/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Ana",
                                  "apellido": "Pérez",
                                  "mail": "ana.config@gimnasio.com",
                                  "password": "password123",
                                  "telefono": "1112345678",
                                  "dni": 40111222
                                }
                                """))
                .andExpect(status().isCreated());

        String token = iniciarSesion("ana.config@gimnasio.com", "password123");

        mockMvc.perform(delete("/profesores/1").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void profesor_noPuedeIniciarSesion() throws Exception {
        crearAdministrador();
        String tokenAdmin = iniciarSesion(EMAIL_ADMIN, PASSWORD_ADMIN);

        mockMvc.perform(post("/profesores")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dni": 30111333,
                                  "email": "profe.sinlogin@gimnasio.com",
                                  "nombre": "Profe Sin Login",
                                  "telefono": "1144556677",
                                  "biografia": "Bio",
                                  "especialidades": "Pilates"
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"profe.sinlogin@gimnasio.com\", \"password\": \"cualquiera123\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(MSG_CREDENCIALES));
    }

    // ------------------------------------------------------------------------
    // Auxiliares
    // ------------------------------------------------------------------------

    private List<Class<?>> clasesDeLosFiltros() {
        List<SecurityFilterChain> cadenas = filterChainProxy.getFilterChains();
        assertEquals(1, cadenas.size(), "Se esperaba una única SecurityFilterChain");

        return cadenas.get(0).getFilters().stream()
                .<Class<?>>map(Object::getClass)
                .toList();
    }

    private void crearAdministrador() {
        usuarioDAO.save(new Administrador(
                20999888L,
                EMAIL_ADMIN,
                passwordEncoder.encode(PASSWORD_ADMIN),
                "Admin Config",
                "1100000000"
        ));
    }

    private String iniciarSesion(String email, String password) throws Exception {
        String respuesta = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return JsonPath.read(respuesta, "$.token");
    }
}
