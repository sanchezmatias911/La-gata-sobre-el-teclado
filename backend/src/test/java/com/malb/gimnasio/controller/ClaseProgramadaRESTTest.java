package com.malb.gimnasio.controller;

import com.malb.gimnasio.controller.REST.ClaseProgramadaREST;
import com.malb.gimnasio.modelo.Actividad;
import com.malb.gimnasio.modelo.usuario.Profesor;
import com.malb.gimnasio.service.ActividadService;
import com.malb.gimnasio.service.ClaseProgramadaService;
import com.malb.gimnasio.service.InscripcionService;
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

@WebMvcTest(ClaseProgramadaREST.class)
class ClaseProgramadaRESTTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClaseProgramadaService claseProgramadaService;

    @MockBean
    private ProfesorService profesorService;

    @MockBean
    private ActividadService actividadService;

    @MockBean
    private InscripcionService inscripcionService;

    @Test
    void recuperarTodos_devuelve200() throws Exception {
        when(claseProgramadaService.recuperarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/clases")).andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void recuperar_noEncontrada_devuelve404() throws Exception {
        when(claseProgramadaService.recuperar(999L)).thenThrow(new NoSuchElementException("Clase no encontrada"));

        mockMvc.perform(get("/clases/999")).andExpect(status().isNotFound());
    }

    @Test
    void crear_jsonValido_devuelve200() throws Exception {
        Profesor profesor = new Profesor(
                23456789L,
                "profesor@test.com",
                "clave123",
                "Ana Lopez",
                "1144556677",
                "Profesora",
                "Yoga"
        );
        profesor.setId(1L);

        Actividad actividad = new Actividad(
                "Yoga",
                "Clase de yoga",
                20
        );
        actividad.setId(1L);

        when(profesorService.recuperar(1L))
                .thenReturn(profesor);

        when(actividadService.recuperar(1L))
                .thenReturn(actividad);

        String json = """
            {
                "dia": "LUNES",
                "horaInicio": "10:00:00",
                "horaFin": "11:00:00",
                "estado": "ACTIVA",
                "idProfesor": 1,
                "idActividad": 1
            }
            """;

        mockMvc.perform(post("/clases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }


    @Test
    void crear_jsonInvalido_devuelve400() throws Exception {
        mockMvc.perform(post("/clases").contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_devuelve200() throws Exception {
        mockMvc.perform(delete("/clases/1")).andExpect(status().isOk());

        verify(claseProgramadaService).eliminar(1L);
    }

    @Test
    void metodoNoPermitido_devuelve405() throws Exception {
        mockMvc.perform(put("/clases/1")).andExpect(status().isMethodNotAllowed());
    }
}