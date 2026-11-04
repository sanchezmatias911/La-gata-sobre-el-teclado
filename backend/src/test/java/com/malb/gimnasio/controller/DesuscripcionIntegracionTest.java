package com.malb.gimnasio.controller;
import com.malb.gimnasio.dao.*;
import com.malb.gimnasio.modelo.*;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.modelo.usuario.Profesor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static com.malb.gimnasio.modelo.Dia.LUNES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DesuscripcionIntegracionTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfesorDAO profesorDAO;

    @Autowired
    private ActividadDAO actividadDAO;

    @Autowired
    private ClaseProgramadaDAO claseProgramadaDAO;

    @Autowired
    private ClienteDAO clienteDAO;

    @Autowired
    private InscripcionDAO inscripcionDAO;

    @Test
    @Transactional
    void desuscribir_eliminaInscripcionDeLaBaseYDelCliente() throws Exception {

        Profesor profesor = new Profesor(
                23456789L,
                "profesor@test.com",
                "Ana Lopez",
                "1144556677",
                "Profesora",
                "Yoga"
        );
        profesor = profesorDAO.save(profesor);

        Actividad actividad = new Actividad(
                "Yoga",
                "Clase de yoga",
                20
        );
        actividad = actividadDAO.save(actividad);

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

        Inscripcion inscripcion = new Inscripcion(cliente, clase);

        clase.agregarInscripcion(inscripcion);
        clase = claseProgramadaDAO.save(clase);

        cliente.agregarInscripcion(inscripcion);
        cliente = clienteDAO.save(cliente);

        inscripcion = inscripcionDAO.save(inscripcion);

        Long idClase = clase.getId();
        Long idCliente = cliente.getId();
        Long idInscripcion = inscripcion.getId();

        mockMvc.perform(delete(
                        "/clases/" + idClase + "/desuscribir/" + idCliente))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idClase));

        assertFalse(inscripcionDAO.findById(idInscripcion).isPresent());

        Cliente clienteActualizado = clienteDAO.findById(idCliente)
                .orElseThrow();

        assertFalse(clienteActualizado.getMisInscripciones().stream()
                .anyMatch(i -> i.getId().equals(idInscripcion)));
    }

    @Test
    @Transactional
    void desuscribir_clienteDeClaseLlena_cambiaEstadoAActiva() throws Exception {
        // Arrange: crear y guardar profesor
        Profesor profesor = new Profesor(
                23456789L,
                "profesor@test.com",
                "Ana Lopez",
                "1144556677",
                "Profesora",
                "Yoga"
        );
        profesor = profesorDAO.save(profesor);

        // Crear actividad con cupo para un solo cliente
        Actividad actividad = new Actividad(
                "Yoga",
                "Clase de yoga",
                1
        );
        actividad = actividadDAO.save(actividad);

        // Crear clase inicialmente activa
        ClaseProgramada clase = new ClaseProgramada(
                Dia.LUNES,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                profesor,
                actividad,
                EstadoClaseProgramada.ACTIVA
        );
        clase = claseProgramadaDAO.save(clase);

        // Crear cliente
        Cliente cliente = new Cliente(
                50111222L,
                "cliente.inscripcion@test.com",
                "password",
                "Cliente Test",
                "1100000000"
        );
        cliente = clienteDAO.save(cliente);

        // Crear inscripción
        Inscripcion inscripcion = new Inscripcion(cliente, clase);
        inscripcion = inscripcionDAO.save(inscripcion);

        // Asociar la inscripción a las entidades
        clase.agregarInscripcion(inscripcion);
        cliente.agregarInscripcion(inscripcion);

        claseProgramadaDAO.save(clase);
        clienteDAO.save(cliente);

        Long idClase = clase.getId();
        Long idCliente = cliente.getId();
        Long idInscripcion = inscripcion.getId();

        // Comprobar que inicialmente está llena
        clase.actualizarEstado();
        assertEquals(EstadoClaseProgramada.LLENA, clase.getEstado());
        assertEquals(1, clase.cantidadInscriptos());

        // Act: desuscribir al cliente
        mockMvc.perform(delete(
                        "/clases/" + idClase + "/desuscribir/" + idCliente
                ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idClase))
                .andExpect(jsonPath("$.estado").value("ACTIVA"))
                .andExpect(jsonPath("$.cantidadInscriptos").value(0));

        // Assert: comprobar el estado persistido
        ClaseProgramada claseActualizada =
                claseProgramadaDAO.findById(idClase).orElseThrow();

        assertEquals(EstadoClaseProgramada.ACTIVA, claseActualizada.getEstado());
        assertEquals(0, claseActualizada.cantidadInscriptos());

        // Comprobar que la inscripción fue eliminada
        assertFalse(inscripcionDAO.findById(idInscripcion).isPresent());
    }
}
