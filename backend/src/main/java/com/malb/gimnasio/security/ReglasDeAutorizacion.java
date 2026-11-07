package com.malb.gimnasio.security;

import com.malb.gimnasio.modelo.usuario.Rol;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

public class ReglasDeAutorizacion
        implements Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> {

    private static final String CLIENTE = Rol.CLIENTE.name();
    private static final String ADMIN = Rol.ADMIN.name();

    private static final String[] DOCUMENTACION_SWAGGER = {"/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"};

    @Override
    public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry reglas) {
        permitirRutasPublicas(reglas);
        exigirClienteOAdmin(reglas);
        exigirSoloAdmin(reglas);
        exigirAdminEnCualquierOtraRuta(reglas);
    }

    private void permitirRutasPublicas(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry reglas) {
        reglas.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        reglas.requestMatchers("/error").permitAll();

        reglas.requestMatchers(HttpMethod.POST, "/auth/login", "/auth/registro").permitAll();

        reglas.requestMatchers(HttpMethod.GET, "/clases", "/clases/*", "/actividades", "/actividades/*").permitAll();

        reglas.requestMatchers("/actuator/health").permitAll();
        reglas.requestMatchers(DOCUMENTACION_SWAGGER).permitAll();
    }

    private void exigirClienteOAdmin(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry reglas) {
        reglas.requestMatchers(HttpMethod.POST, "/clases/*/suscripcion").hasAnyRole(CLIENTE, ADMIN);
        reglas.requestMatchers(HttpMethod.DELETE, "/clases/*/desuscribir/*").hasAnyRole(CLIENTE, ADMIN);

        reglas.requestMatchers(HttpMethod.GET, "/clientes/*").hasAnyRole(CLIENTE, ADMIN);
    }

    private void exigirSoloAdmin(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry reglas) {
        reglas.requestMatchers("/profesores/**").hasRole(ADMIN);
        reglas.requestMatchers("/clientes/**").hasRole(ADMIN);

        reglas.requestMatchers(HttpMethod.POST, "/clases", "/actividades").hasRole(ADMIN);
        reglas.requestMatchers(HttpMethod.DELETE, "/clases/*", "/actividades/*").hasRole(ADMIN);

        reglas.requestMatchers("/actuator/**").hasRole(ADMIN);
    }

    private void exigirAdminEnCualquierOtraRuta(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry reglas) {
        reglas.anyRequest().hasRole(ADMIN);
    }
}
