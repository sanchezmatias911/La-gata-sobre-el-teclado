package com.malb.gimnasio.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.malb.gimnasio.controller.DTO.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Quitar (o deshabilitar springdoc) en producción.
    private static final String[] SWAGGER = {"/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"};

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter, ObjectMapper mapper) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable).cors(Customizer.withDefaults()).httpBasic(AbstractHttpConfigurer::disable).formLogin(AbstractHttpConfigurer::disable).sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).exceptionHandling(eh -> eh.authenticationEntryPoint((req, res, ex) -> escribir(res, mapper, 401, mensajeNoAutenticado(req))).accessDeniedHandler((req, res, ex) -> escribir(res, mapper, 403, "No tenés permisos para realizar esta acción."))).authorizeHttpRequests(auth -> auth

                // -------------------------------------------------
                // ENDPOINTS PÚBLICOS
                // -------------------------------------------------

                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll().requestMatchers("/error").permitAll()

                .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/registro").permitAll()

                .requestMatchers(HttpMethod.GET, "/clases", "/clases/*", "/actividades", "/actividades/*").permitAll()

                .requestMatchers("/actuator/health").permitAll()

                // Swagger solo disponible en desarrollo.
                .requestMatchers(SWAGGER).permitAll()

                // -------------------------------------------------
                // CLIENTE O ADMIN
                // -------------------------------------------------

                .requestMatchers(HttpMethod.POST, "/clases/*/suscripcion").hasAnyRole("CLIENTE", "ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/clases/*/desuscribir/*").hasAnyRole("CLIENTE", "ADMIN")

                .requestMatchers(HttpMethod.GET, "/clientes/*").hasAnyRole("CLIENTE", "ADMIN")

                // -------------------------------------------------
                // EXCLUSIVO ADMIN
                // -------------------------------------------------

                .requestMatchers("/profesores/**").hasRole("ADMIN").requestMatchers("/clientes/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/clases", "/actividades").hasRole("ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/clases/*", "/actividades/*").hasRole("ADMIN")

                // -------------------------------------------------
                // ACTUATOR
                // -------------------------------------------------

                .requestMatchers("/actuator/**").hasRole("ADMIN")

                // -------------------------------------------------
                // CUALQUIER ENDPOINT NUEVO: ADMIN
                // -------------------------------------------------

                .anyRequest().hasRole("ADMIN")).addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource(@Value("${cors.allowed-origins:http://localhost:5173}") String[] origenes) {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(Arrays.asList(origenes));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    /** 401: no se mandó token / el token venció / el token es inválido (contrato). */
    private String mensajeNoAutenticado(HttpServletRequest req) {
        Object motivo = req.getAttribute(JwtAuthFilter.ATRIBUTO_ERROR_TOKEN);

        if (JwtAuthFilter.TOKEN_VENCIDO.equals(motivo)) {
            return "Tu sesión venció. Iniciá sesión nuevamente.";
        }
        if (JwtAuthFilter.TOKEN_INVALIDO.equals(motivo)) {
            return "Token inválido.";
        }
        return "Necesitás iniciar sesión para acceder a este recurso.";
    }

    private void escribir(HttpServletResponse res, ObjectMapper mapper, int status, String msg) throws IOException {
        res.setStatus(status);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setCharacterEncoding("UTF-8");
        mapper.writeValue(res.getWriter(), new ApiError(status, msg));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}