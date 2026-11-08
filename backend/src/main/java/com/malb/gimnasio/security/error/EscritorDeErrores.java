package com.malb.gimnasio.security.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.malb.gimnasio.controller.DTO.ApiError;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EscritorDeErrores {

    private final ObjectMapper mapper;

    public EscritorDeErrores(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void escribir(HttpServletResponse response, HttpStatus estado, String mensaje) throws IOException {
        response.setStatus(estado.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        mapper.writeValue(response.getWriter(), new ApiError(estado.value(), mensaje));
    }
}
