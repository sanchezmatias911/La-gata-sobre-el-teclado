package com.malb.gimnasio.controller;

import com.malb.gimnasio.controller.REST.ProfesorREST;
import com.malb.gimnasio.modelo.usuario.Profesor;
import com.malb.gimnasio.service.ProfesorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.NoSuchElementException;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfesorREST.class)
class ProfesorRESTTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfesorService profesorService;

    @Test
    void recuperarTodos_devuelve200() throws Exception {
        when(profesorService.recuperarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/profesores")).andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void recuperar_noEncontrado_devuelve404() throws Exception {
        when(profesorService.recuperar(999L)).thenThrow(new NoSuchElementException("Profesor no encontrado"));

        mockMvc.perform(get("/profesores/999")).andExpect(status().isNotFound());
    }

    @Test
    void crear_jsonValido_devuelve200() throws Exception {
        String json = """
                {
                    "dni": 23456789,
                    "email": "profesor@test.com",
                    "password": "clave123",
                    "nombre": "Ana Lopez",
                    "telefono": "1144556677",
                    "biografia": "Profesora de gimnasio",
                    "especialidades": "Yoga y pilates"
                }
                """;

        mockMvc.perform(post("/profesores").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());

        verify(profesorService).crear(any(Profesor.class));
    }

    @Test
    void crear_jsonInvalido_devuelve400() throws Exception {
        String json = """
                {
                    "dni": 23456789,
                    "email": "",
                    "password": "",
                    "nombre": "",
                    "telefono": "",
                    "biografia": "",
                    "especialidades": ""
                }
                """;

        mockMvc.perform(post("/profesores").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_devuelve200() throws Exception {
        mockMvc.perform(delete("/profesores/23456789")).andExpect(status().isOk());

        verify(profesorService).eliminar(23456789L);
    }

    @Test
    void metodoNoPermitido_devuelve405() throws Exception {
        mockMvc.perform(put("/profesores/23456789")).andExpect(status().isMethodNotAllowed());
    }
}