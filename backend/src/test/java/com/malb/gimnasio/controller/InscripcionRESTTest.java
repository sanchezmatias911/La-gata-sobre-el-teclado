package com.malb.gimnasio.controller;

import com.malb.gimnasio.controller.REST.InscripcionREST;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.service.InscripcionService;
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

@WebMvcTest(InscripcionREST.class)
class InscripcionRESTTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InscripcionService inscripcionService;

    @Test
    void recuperarTodos_devuelve200() throws Exception {
        when(inscripcionService.recuperarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/inscripciones")).andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void recuperar_noEncontrada_devuelve404() throws Exception {
        when(inscripcionService.recuperar(999L)).thenThrow(new NoSuchElementException("Inscripción no encontrada"));

        mockMvc.perform(get("/inscripciones/999")).andExpect(status().isNotFound());
    }

    @Test
    void crear_jsonValido_devuelve200() throws Exception {
        String json = """
                {
                    "id": 1,
                    "fechaDeInscripcion": "2026-10-15T10:00:00",
                    "cliente": {
                        "dni": 12345678,
                        "email": "cliente@test.com",
                        "password": "clave123",
                        "nombre": "Juan Perez",
                        "telefono": "1122334455",
                        "misInscripciones": []
                    },
                    "claseProgramada": {
                        "id": 1,
                        "dia": "LUNES",
                        "horaInicio": "10:00:00",
                        "horaFin": "11:00:00",
                        "estado": "ACTIVA",
                        "profesor": {
                            "dni": 23456789,
                            "email": "profesor@test.com",
                            "password": "clave123",
                            "nombre": "Ana Lopez",
                            "telefono": "1144556677",
                            "biografia": "Profesora",
                            "especialidades": "Yoga"
                        },
                        "actividad": {
                            "id": 1,
                            "nombre": "Yoga",
                            "descripcion": "Clase de yoga",
                            "cupo": 20
                        }
                    }
                }
                """;

        mockMvc.perform(post("/inscripciones").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());

        verify(inscripcionService).crear(any(Inscripcion.class));
    }

    @Test
    void crear_jsonInvalido_devuelve400() throws Exception {
        mockMvc.perform(post("/inscripciones").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_devuelve200() throws Exception {
        mockMvc.perform(delete("/inscripciones/1")).andExpect(status().isOk());

        verify(inscripcionService).eliminar(1L);
    }

    @Test
    void metodoNoPermitido_devuelve405() throws Exception {
        mockMvc.perform(put("/inscripciones/1")).andExpect(status().isMethodNotAllowed());
    }
}