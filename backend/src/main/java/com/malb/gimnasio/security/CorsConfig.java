package com.malb.gimnasio.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.List;

@Configuration
public class CorsConfig {

    private static final List<String> METODOS_PERMITIDOS = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
    private static final List<String> ENCABEZADOS_PERMITIDOS = List.of("Authorization", "Content-Type");
    private static final Duration VIGENCIA_DEL_PREFLIGHT = Duration.ofHours(1);

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${cors.allowed-origins:http://localhost:5173}") String[] origenesPermitidos
    ) {
        CorsConfiguration configuracion = new CorsConfiguration();
        configuracion.setAllowedOrigins(List.of(origenesPermitidos));
        configuracion.setAllowedMethods(METODOS_PERMITIDOS);
        configuracion.setAllowedHeaders(ENCABEZADOS_PERMITIDOS);
        configuracion.setMaxAge(VIGENCIA_DEL_PREFLIGHT);

        UrlBasedCorsConfigurationSource fuente = new UrlBasedCorsConfigurationSource();
        fuente.registerCorsConfiguration("/**", configuracion);
        return fuente;
    }
}
