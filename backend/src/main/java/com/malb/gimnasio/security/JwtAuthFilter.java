package com.malb.gimnasio.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //todo IMPLEMENTAR, Corre en cada request.
        // Si trae un token válido, deja al usuario “logueado” para ese request;
        // si no, sigue como anónimo.
        // Quien decide si eso alcanza es SecurityConfig.
    }
}
