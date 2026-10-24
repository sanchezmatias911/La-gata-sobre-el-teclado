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
    void desuscribir_eliminaInscripcionDeLaBaseYDelCliente()
            throws Exception {

        Profesor profesor = new Profesor(
                23456789L,
                "profesor@test.com",
                "clave123",
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
                LUNES,
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

        mockMvc.perform(delete(
                        "/clases/" + idClase + "/desuscribir/" + idCliente))
                .andExpect(status().isNoContent());

        assertFalse(inscripcionDAO.findById(inscripcion.getId()).isPresent());

        Cliente clienteActualizado = clienteDAO.findById(idCliente)
                .orElseThrow();

        Inscripcion finalInscripcion = inscripcion;
        assertFalse(clienteActualizado.getMisInscripciones().stream()
                .anyMatch(i -> i.getId().equals(finalInscripcion.getId())));
    }
}
