package com.malb.gimnasio.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.malb.gimnasio.controller.DTO.ApiError;
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
        http.csrf(AbstractHttpConfigurer::disable)          // sin cookies de sesión no hay CSRF clásico
                .cors(Customizer.withDefaults())                // usa el bean corsConfigurationSource
                .httpBasic(AbstractHttpConfigurer::disable).formLogin(AbstractHttpConfigurer::disable).sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).exceptionHandling(eh -> eh.authenticationEntryPoint((req, res, ex) -> escribir(res, mapper, 401, "Necesitás iniciar sesión para acceder a este recurso.")).accessDeniedHandler((req, res, ex) -> escribir(res, mapper, 403, "No tenés permisos para realizar esta acción."))).authorizeHttpRequests(auth -> auth
                        // ---------- PÚBLICO (sin token) ----------
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll().requestMatchers("/error").permitAll().requestMatchers(HttpMethod.POST, "/auth/registro", "/auth/login").permitAll().requestMatchers(HttpMethod.GET, "/clases", "/clases/*", "/actividades", "/actividades/*").permitAll().requestMatchers("/actuator/health").permitAll().requestMatchers(SWAGGER).permitAll()

                        // ---------- CLIENTE o ADMIN (token). El "solo lo tuyo" se valida en el controller ----------
                        .requestMatchers(HttpMethod.POST, "/clases/*/suscripcion").hasAnyRole("CLIENTE", "ADMIN").requestMatchers(HttpMethod.DELETE, "/clases/*/desuscribir/*").hasAnyRole("CLIENTE", "ADMIN").requestMatchers(HttpMethod.GET, "/clientes/*").hasAnyRole("CLIENTE", "ADMIN")

                        // ---------- TODO LO DEMÁS: solo ADMIN ----------
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

    private void escribir(HttpServletResponse res, ObjectMapper mapper, int status, String msg) throws IOException {
        res.setStatus(status);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setCharacterEncoding("UTF-8");
        mapper.writeValue(res.getWriter(), new ApiError(status, msg));
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }
}