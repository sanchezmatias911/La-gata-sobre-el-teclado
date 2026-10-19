package com.malb.gimnasio.controller;

import com.malb.gimnasio.controller.REST.ActividadREST;
import com.malb.gimnasio.modelo.Actividad;
import com.malb.gimnasio.service.ActividadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ActividadREST.class)
class ActividadRESTTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ActividadService actividadService;

    @Test
    void recuperarTodos_devuelve200() throws Exception {
        when(actividadService.recuperarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/actividades")).andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void recuperar_noEncontrada_devuelve404() throws Exception {
        when(actividadService.recuperar(99L)).thenThrow(new NoSuchElementException("Actividad no encontrada"));

        mockMvc.perform(get("/actividades/99")).andExpect(status().isNotFound());
    }

    @Test
    void crear_jsonValido_devuelve200() throws Exception {
        String json = """
                {
                    "id": 1,
                    "nombre": "Yoga",
                    "descripcion": "Clase de yoga",
                    "cupo": 20
                }
                """;

        mockMvc.perform(post("/actividades").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());

        verify(actividadService).crear(any(Actividad.class));
    }

    @Test
    void crear_jsonInvalido_devuelve400() throws Exception {
        String json = """
                {
                    "id": 1,
                    "nombre": "",
                    "descripcion": "",
                    "cupo": 0
                }
                """;

        mockMvc.perform(post("/actividades").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_devuelve200() throws Exception {
        doNothing().when(actividadService).eliminar(1L);

        mockMvc.perform(delete("/actividades/1")).andExpect(status().isOk());

        verify(actividadService).eliminar(1L);
    }

    @Test
    void metodoNoPermitido_devuelve405() throws Exception {
        mockMvc.perform(put("/actividades/1")).andExpect(status().isMethodNotAllowed());
    }
}