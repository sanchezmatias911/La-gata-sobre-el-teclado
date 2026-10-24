package com.malb.gimnasio.controller;

import com.malb.gimnasio.controller.REST.ClaseProgramadaREST;
import com.malb.gimnasio.modelo.Actividad;
import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Dia;
import com.malb.gimnasio.modelo.EstadoClaseProgramada;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.modelo.usuario.Profesor;
import com.malb.gimnasio.service.ActividadService;
import com.malb.gimnasio.service.ClaseProgramadaService;
import com.malb.gimnasio.service.InscripcionService;
import com.malb.gimnasio.service.ProfesorService;
import com.malb.gimnasio.service.ClienteService;
import com.malb.gimnasio.modelo.exception.InscripcionNoPermitidaException;
import com.malb.gimnasio.modelo.exception.RecursoDuplicadoException;
import com.malb.gimnasio.service.exception.RecursoNoEncontradoException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
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

    @MockBean
    private ClienteService clienteService;

    @Test
    void recuperarTodos_devuelve200() throws Exception {
        when(claseProgramadaService.recuperarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/clases"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void recuperar_noEncontrada_devuelve404() throws Exception {
        when(claseProgramadaService.recuperar(999L))
                .thenThrow(new NoSuchElementException("Clase no encontrada"));

        mockMvc.perform(get("/clases/999"))
                .andExpect(status().isNotFound());
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

        when(profesorService.recuperar(1L)).thenReturn(profesor);
        when(actividadService.recuperar(1L)).thenReturn(actividad);

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
        mockMvc.perform(post("/clases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminar_devuelve200() throws Exception {
        mockMvc.perform(delete("/clases/1"))
                .andExpect(status().isOk());

        verify(claseProgramadaService).eliminar(1L);
    }

    @Test
    void metodoNoPermitido_devuelve405() throws Exception {
        mockMvc.perform(put("/clases/1"))
                .andExpect(status().isMethodNotAllowed());
    }

    // =========================
    // SUSCRIBIRSE A UNA CLASE
    // =========================

    // CASO BUENO: suscripción exitosa.
    @Test
    void suscribir_clienteValidoYClaseValida_devuelve204() throws Exception {
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

        when(profesorService.recuperar(1L)).thenReturn(profesor);
        when(actividadService.recuperar(1L)).thenReturn(actividad);

        ClaseProgramada clase = new ClaseProgramada(
                Dia.LUNES,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                profesor,
                actividad,
                EstadoClaseProgramada.ACTIVA
        );

        Cliente cliente = new Cliente(
                50111222L,
                "cliente.inscripcion@test.com",
                "password",
                "Cliente Test",
                "1100000000"
        );

        when(clienteService.recuperar(1L)).thenReturn(cliente);
        when(claseProgramadaService.recuperar(10L)).thenReturn(clase);

        String json = """
            {
                "idCliente": 1
            }
            """;

        mockMvc.perform(post("/clases/10/suscripcion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());

        verify(clienteService).recuperar(1L);
        verify(claseProgramadaService).recuperar(10L);
        verify(inscripcionService).crear(cliente, clase);
    }

    // CASO MALO: el JSON no contiene el idCliente requerido.
    @Test
    void suscribir_sinIdCliente_devuelve400() throws Exception {
        mockMvc.perform(post("/clases/10/suscripcion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(inscripcionService);
    }

    // CASO MALO: el JSON está mal formado.
    @Test
    void suscribir_jsonMalFormado_devuelve400() throws Exception {
        mockMvc.perform(post("/clases/10/suscripcion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "idCliente":
                            """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(inscripcionService);
    }

    // CASO MALO: el cliente no existe.
    @Test
    void suscribir_clienteInexistente_devuelve404() throws Exception {
        when(clienteService.recuperar(999L))
                .thenThrow(new RecursoNoEncontradoException("Cliente no encontrado"));

        String json = """
            {
                "idCliente": 999
            }
            """;

        mockMvc.perform(post("/clases/10/suscripcion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());

        verify(clienteService).recuperar(999L);
        verifyNoInteractions(inscripcionService);
    }

    // CASO MALO: la clase no existe.
    @Test
    void suscribir_claseInexistente_devuelve404() throws Exception {

        when(claseProgramadaService.recuperar(999L))
                .thenThrow(new RecursoNoEncontradoException("Clase no encontrada"));

        String json = """
            {
                "idCliente": 1
            }
            """;

        mockMvc.perform(post("/clases/999/suscripcion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());

        verify(claseProgramadaService).recuperar(999L);
        verifyNoInteractions(inscripcionService);
    }

    // CASO MALO: la clase no admite nuevas inscripciones.
    @Test
    void suscribir_claseNoDisponible_devuelve409() throws Exception {
        Cliente cliente = mock(Cliente.class);
        ClaseProgramada clase = mock(ClaseProgramada.class);

        when(clienteService.recuperar(1L)).thenReturn(cliente);
        when(claseProgramadaService.recuperar(10L)).thenReturn(clase);

        doThrow(new InscripcionNoPermitidaException(
                "La clase no está disponible para nuevas inscripciones"
        )).when(inscripcionService).crear(cliente, clase);

        String json = """
            {
                "idCliente": 1
            }
            """;

        mockMvc.perform(post("/clases/10/suscripcion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());

        verify(inscripcionService).crear(cliente, clase);
    }

    // CASO MALO: el cliente ya está inscripto en esa clase.
    @Test
    void suscribir_clienteYaInscripto_devuelve409() throws Exception {
        Cliente cliente = mock(Cliente.class);
        ClaseProgramada clase = mock(ClaseProgramada.class);

        when(clienteService.recuperar(1L)).thenReturn(cliente);
        when(claseProgramadaService.recuperar(10L)).thenReturn(clase);

        doThrow(new RecursoDuplicadoException(
                "El cliente ya está inscripto en esta clase"
        )).when(inscripcionService).crear(cliente, clase);

        String json = """
            {
                "idCliente": 1
            }
            """;

        mockMvc.perform(post("/clases/10/suscripcion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());

        verify(inscripcionService).crear(cliente, clase);
    }

    // CASO MALO: se utiliza GET en lugar de POST.
    @Test
    void suscribir_conMetodoGet_devuelve405() throws Exception {
        mockMvc.perform(get("/clases/10/suscripcion")).andExpect(status().isMethodNotAllowed());

        verifyNoInteractions(inscripcionService);
    }
}