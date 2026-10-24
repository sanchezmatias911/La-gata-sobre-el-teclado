
package com.malb.gimnasio.service;

import com.malb.gimnasio.modelo.Actividad;
import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.Dia;
import com.malb.gimnasio.modelo.EstadoClaseProgramada;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.modelo.usuario.Profesor;
import com.malb.gimnasio.service.exception.RecursoDuplicadoException;
import com.malb.gimnasio.service.exception.RecursoNoEncontradoException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class InscripcionServiceTest {

    @Autowired
    private InscripcionService inscripcionService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProfesorService profesorService;

    @Autowired
    private ActividadService actividadService;

    @Autowired
    private ClaseProgramadaService claseProgramadaService;

    private Cliente cliente1, cliente2;
    private ClaseProgramada clase, clase2;
    private Profesor profesor;
    private Actividad actividad;

    @BeforeEach
    void setUp() {
        cliente1 = new Cliente(
                50111222L,
                "cliente1.inscripcion@test.com",
                "password",
                "Cliente Test",
                "1100000000"
        );

        cliente2 = new Cliente(
                50111333L,
                "cliente2.inscripcion@test.com",
                "password",
                "Cliente Test",
                "1100000000"
        );

        profesor = new Profesor(
                23456789L,
                "profesor.inscripcion@test.com",
                "password",
                "Profesor Test",
                "1144556677",
                "Profesor",
                "Yoga"
        );

        actividad = new Actividad(
                "Yoga Test",
                "Actividad para pruebas",
                20
        );

        clase = new ClaseProgramada(
                Dia.LUNES,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                profesor,
                actividad,
                EstadoClaseProgramada.ACTIVA
        );

        clase2 = new ClaseProgramada(
                Dia.LUNES,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                profesor,
                actividad,
                EstadoClaseProgramada.ACTIVA
        );

        clienteService.crear(cliente1);
        clienteService.crear(cliente2);
        profesorService.crear(profesor);
        actividadService.crear(actividad);
        claseProgramadaService.crear(clase);
    }

    @Test
    void crear_inscripcionValida_guardaLaInscripcion() {
        Inscripcion inscripcion = inscripcionService.crear(cliente2, clase2);

        assertNotNull(inscripcion);
        assertNotNull(inscripcion.getId());
        assertEquals(cliente2.getId(), inscripcion.getCliente().getId());
        assertEquals(clase2.getId(), inscripcion.getClaseProgramada().getId());
    }

    @Test
    void crear_cliente1YaInscripto_lanzaExcepcion() {
        inscripcionService.crear(cliente1, clase);

        assertThrows(
                RecursoDuplicadoException.class,
                () -> inscripcionService.crear(cliente1, clase)
        );
    }

    @Test
    void crear_claseNoDisponible_lanzaExcepcion() {
        //TODO IMPLEMENTAR CUANDO SE CAMBIEN LOS ESTADOS
    }

    @Test
    void recuperar_inscripcionExistente_devuelveInscripcion() {
        Inscripcion creada =
                inscripcionService.crear(cliente1, clase);

        Inscripcion recuperada =
                inscripcionService.recuperar(creada.getId());

        assertNotNull(recuperada);
        assertEquals(creada.getId(), recuperada.getId());
        assertEquals(cliente1.getId(), recuperada.getCliente().getId());
        assertEquals(clase.getId(),
                recuperada.getClaseProgramada().getId());
    }

    @Test
    void recuperar_idInexistente_lanzaExcepcion() {
        assertThrows(
                RecursoNoEncontradoException.class,
                () -> inscripcionService.recuperar(-1L)
        );
    }

    @Test
    void recuperar_idNulo_lanzaExcepcion() {
        assertThrows(
                IllegalArgumentException.class,
                () -> inscripcionService.recuperar(null)
        );
    }

    @Test
    void eliminar_inscripcionExistente_eliminaLaInscripcion() {
        Inscripcion creada =
                inscripcionService.crear(cliente1, clase);

        Long id = creada.getId();

        inscripcionService.eliminar(id);

        assertThrows(
                RecursoNoEncontradoException.class,
                () -> inscripcionService.recuperar(id)
        );
    }

    @Test
    void eliminar_idInexistente_lanzaExcepcion() {
        assertThrows(
                RecursoNoEncontradoException.class,
                () -> inscripcionService.eliminar(-1L)
        );
    }

    @Test
    void eliminar_idNulo_lanzaExcepcion() {
        assertThrows(
                IllegalArgumentException.class,
                () -> inscripcionService.eliminar(null)
        );
    }

/*
    @AfterEach
    void tearDown() {
        inscripcionService.eliminarTodos();
        claseProgramadaService.eliminarTodos();
        actividadService.eliminarTodos();
        profesorService.eliminarTodos();
        clienteService.eliminarTodos();
    }*/

}