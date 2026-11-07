package com.malb.gimnasio.controller;

import com.malb.gimnasio.controller.REST.ClienteREST;
import com.malb.gimnasio.security.CustomUserDetailsService;
import com.malb.gimnasio.security.JwtService;
import com.malb.gimnasio.security.WithUsuarioAutenticado;
import com.malb.gimnasio.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteREST.class)
@WithUsuarioAutenticado
class ClienteRESTTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;
    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void recuperarTodos_devuelve200() throws Exception {
        when(clienteService.recuperarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/clientes")).andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void recuperar_noEncontrado_devuelve404() throws Exception {
        when(clienteService.recuperar(999L)).thenThrow(new NoSuchElementException("Cliente no encontrado"));

        mockMvc.perform(get("/clientes/999")).andExpect(status().isNotFound());
    }

    @Test
    void crear_jsonInvalido_devuelve400() throws Exception {
        String json = """
                {
                    "dni": 12345678,
                    "email": "",
                    "password": "",
                    "nombre": "",
                    "telefono": "",
                    "misInscripciones": []
                }
                """;

        mockMvc.perform(post("/clientes").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_devuelve200() throws Exception {
        mockMvc.perform(delete("/clientes/12345678").with(csrf())).andExpect(status().isOk());

        verify(clienteService).eliminar(12345678L);
    }

    @Test
    void metodoNoPermitido_devuelve405() throws Exception {
        mockMvc.perform(put("/clientes/12345678").with(csrf())).andExpect(status().isMethodNotAllowed());
    }
}