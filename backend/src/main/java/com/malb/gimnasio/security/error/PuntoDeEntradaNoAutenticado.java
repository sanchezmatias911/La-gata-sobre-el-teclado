package com.malb.gimnasio.security.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class PuntoDeEntradaNoAutenticado implements AuthenticationEntryPoint {

    private final EscritorDeErrores escritor;

    public PuntoDeEntradaNoAutenticado(EscritorDeErrores escritor) {
        this.escritor = escritor;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException excepcion
    ) throws IOException {
        escritor.escribir(response, HttpStatus.UNAUTHORIZED, mensajeSegunMotivo(request));
    }

    private String mensajeSegunMotivo(HttpServletRequest request) {
        return MotivoRechazoJwt.registradoEn(request)
                .map(MotivoRechazoJwt::mensaje)
                .orElse(MensajesDeSeguridad.SIN_TOKEN);
    }
}
