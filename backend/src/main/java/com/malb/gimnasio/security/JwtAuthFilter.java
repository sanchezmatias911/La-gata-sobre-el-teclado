
package com.malb.gimnasio.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    /** Atributo del request con el motivo por el que se rechazó el token (lo lee SecurityConfig). */
    public static final String ATRIBUTO_ERROR_TOKEN = "jwt.error";
    public static final String TOKEN_VENCIDO = "VENCIDO";
    public static final String TOKEN_INVALIDO = "INVALIDO";

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            String email = jwtService.extraerEmail(token);

            if (email != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails usuario =
                        userDetailsService.loadUserByUsername(email);

                if (jwtService.esTokenValido(token, usuario)
                        && usuario instanceof CustomUserDetails detalles) {

                    UsuarioAutenticado principal = new UsuarioAutenticado(
                            detalles.getId(),
                            detalles.getEmail(),
                            detalles.getRol()
                    );

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    principal,
                                    null,
                                    usuario.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                } else {
                    request.setAttribute(ATRIBUTO_ERROR_TOKEN, TOKEN_INVALIDO);
                }
            }

        } catch (ExpiredJwtException e) {
            SecurityContextHolder.clearContext();
            request.setAttribute(ATRIBUTO_ERROR_TOKEN, TOKEN_VENCIDO);
        } catch (JwtException | IllegalArgumentException
                 | UsernameNotFoundException e) {
            SecurityContextHolder.clearContext();
            request.setAttribute(ATRIBUTO_ERROR_TOKEN, TOKEN_INVALIDO);
        }

        filterChain.doFilter(request, response);
    }
}
