package com.malb.gimnasio.controller;

import com.malb.gimnasio.controller.REST.InscripcionREST;
import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.service.ClaseProgramadaService;
import com.malb.gimnasio.service.ClienteService;
import com.malb.gimnasio.service.InscripcionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InscripcionREST.class)
class InscripcionRESTTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InscripcionService inscripcionService;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private ClaseProgramadaService claseProgramadaService;

    @Test
    void recuperarTodos_devuelve200() throws Exception {
        when(inscripcionService.recuperarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/inscripciones"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void recuperar_noEncontrada_devuelve404() throws Exception {
        when(inscripcionService.recuperar(999L))
                .thenThrow(new NoSuchElementException("Inscripción no encontrada"));

        mockMvc.perform(get("/inscripciones/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_jsonValido_devuelve200() throws Exception {
        Cliente cliente = mock(Cliente.class);
        ClaseProgramada clase = mock(ClaseProgramada.class);

        when(clienteService.recuperar(12345678L)).thenReturn(cliente);
        when(claseProgramadaService.recuperar(1L)).thenReturn(clase);

        when(cliente.getDni()).thenReturn(12345678L);
        when(clase.getId()).thenReturn(1L);

        Inscripcion inscripcion = new Inscripcion(cliente, clase);

        String json = """
                {
                    "idCliente": 12345678,
                    "idClaseProgramada": 1
                }
                """;

        mockMvc.perform(post("/inscripciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(clienteService).recuperar(12345678L);
        verify(claseProgramadaService).recuperar(1L);
        verify(inscripcionService).crear(any(Inscripcion.class));
    }

    @Test
    void crear_jsonInvalido_devuelve400() throws Exception {
        mockMvc.perform(post("/inscripciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(inscripcionService, clienteService, claseProgramadaService);
    }

    @Test
    void eliminar_devuelve200() throws Exception {
        mockMvc.perform(delete("/inscripciones/1"))
                .andExpect(status().isOk());

        verify(inscripcionService).eliminar(1L);
    }

    @Test
    void metodoNoPermitido_devuelve405() throws Exception {
        mockMvc.perform(put("/inscripciones/1"))
                .andExpect(status().isMethodNotAllowed());
    }
}