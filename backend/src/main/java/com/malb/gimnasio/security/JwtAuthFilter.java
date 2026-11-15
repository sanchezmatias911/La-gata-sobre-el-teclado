package com.malb.gimnasio.security;

import com.malb.gimnasio.security.error.MotivoRechazoJwt;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String PREFIJO_BEARER = "Bearer ";

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (!hayUsuarioAutenticado()) {
            extraerToken(request).ifPresent(token -> autenticar(token, request));
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> extraerToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(encabezado -> encabezado.startsWith(PREFIJO_BEARER))
                .map(encabezado -> encabezado.substring(PREFIJO_BEARER.length()));
    }

    private void autenticar(String token, HttpServletRequest request) {
        try {
            CustomUserDetails usuario = cargarUsuarioDelToken(token);

            if (jwtService.esTokenValido(token, usuario)) {
                establecerAutenticacion(usuario, request);
            } else {
                rechazar(request, MotivoRechazoJwt.INVALIDO);
            }
        } catch (ExpiredJwtException e) {
            rechazar(request, MotivoRechazoJwt.VENCIDO);
        } catch (JwtException | IllegalArgumentException | UsernameNotFoundException e) {
            rechazar(request, MotivoRechazoJwt.INVALIDO);
        }
    }

    private CustomUserDetails cargarUsuarioDelToken(String token) {
        String email = jwtService.extraerEmail(token);

        if (email == null) {
            throw new JwtException("El token no tiene usuario.");
        }

        return userDetailsService.loadUserByUsername(email);
    }

    private void establecerAutenticacion(CustomUserDetails usuario, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken autenticacion = new UsernamePasswordAuthenticationToken(
                UsuarioAutenticado.desde(usuario),
                null,
                usuario.getAuthorities()
        );
        autenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(autenticacion);
    }

    private void rechazar(HttpServletRequest request, MotivoRechazoJwt motivo) {
        SecurityContextHolder.clearContext();
        motivo.registrarEn(request);
    }

    private boolean hayUsuarioAutenticado() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }
}
