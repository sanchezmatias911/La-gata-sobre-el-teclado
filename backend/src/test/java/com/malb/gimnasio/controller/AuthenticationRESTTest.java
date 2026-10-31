package com.malb.gimnasio.controller;

import com.malb.gimnasio.controller.DTO.request.RegistroClienteDTO;
import com.malb.gimnasio.controller.REST.AuthenticationREST;
import com.malb.gimnasio.modelo.exception.RecursoDuplicadoException;
import com.malb.gimnasio.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationREST.class)
class AuthenticationRESTTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    private static final String URL = "/auth/registro";

    private static final String json = """
    {
        "nombre": "Maria",
        "apellido": "Perez",
        "mail": "maria@gmail.com",
        "contraseña": "micontra123",
        "telefono": "1123456789",
        "dni": 12345678
    }
    """;

    @Test
    void registroConDatosValidosDevuelve201() throws Exception {
        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());

        verify(authenticationService).registrar(any(RegistroClienteDTO.class));
    }

    @Test
    void registroConMailInvalidoDevuelve400() throws Exception {
        String json = AuthenticationRESTTest.json.replace(
                "maria@gmail.com", "mail-invalido"
        );

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    void registroConContraseñaCortaDevuelve400() throws Exception {
        String json = AuthenticationRESTTest.json.replace(
                "micontra123", "abc123"
        );

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    void registroConTelefonoCortoDevuelve400() throws Exception {
        String json = AuthenticationRESTTest.json.replace(
                "1123456789", "12345"
        );

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    void registroConNombreVacioDevuelve400() throws Exception {
        String json = AuthenticationRESTTest.json.replace(
                "\"nombre\": \"Maria\"",
                "\"nombre\": \"\""
        );

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    void registroConApellidoVacioDevuelve400() throws Exception {
        String json = AuthenticationRESTTest.json.replace(
                "\"apellido\": \"Perez\"",
                "\"apellido\": \" \""
        );

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authenticationService);
    }

    @Test
    void registroConMailExistenteDevuelve409() throws Exception {
        doThrow(new RecursoDuplicadoException("Ya existe un usuario registrado con ese mail"))
                .when(authenticationService).registrar(any(RegistroClienteDTO.class));

        mockMvc.perform(post(URL).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isConflict());

        verify(authenticationService).registrar(any(RegistroClienteDTO.class));
    }
}