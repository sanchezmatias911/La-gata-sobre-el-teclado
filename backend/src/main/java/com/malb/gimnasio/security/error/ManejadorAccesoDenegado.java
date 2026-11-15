package com.malb.gimnasio.security.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class ManejadorAccesoDenegado implements AccessDeniedHandler {

    private final EscritorDeErrores escritor;

    public ManejadorAccesoDenegado(EscritorDeErrores escritor) {
        this.escritor = escritor;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException excepcion
    ) throws IOException {
        escritor.escribir(response, HttpStatus.FORBIDDEN, MensajesDeSeguridad.ACCESO_DENEGADO);
    }
}
