
package com.malb.gimnasio.service;

import com.malb.gimnasio.modelo.Actividad;
import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.Dia;
import com.malb.gimnasio.modelo.EstadoClaseProgramada;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.modelo.usuario.Profesor;
import com.malb.gimnasio.service.exception.RecursoNoEncontradoException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional //VER PORQUE ROMPE SIN TRANSACTIONAL, TIENE QUE VER CON LAS CLAVES FORÁNEAS
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

    private Cliente cliente;
    private ClaseProgramada clase;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(
                50111222L,
                "cliente.inscripcion@test.com",
                "password",
                "Cliente Test",
                "1100000000"
        );

        clienteService.crear(cliente);

        Profesor profesor = new Profesor(
                23456789L,
                "profesor.inscripcion@test.com",
                "password",
                "Profesor Test",
                "1144556677",
                "Profesor",
                "Yoga"
        );

        profesorService.crear(profesor);

        Actividad actividad = new Actividad(
                "Yoga Test",
                "Actividad para pruebas",
                20
        );

        actividadService.crear(actividad);

        clase = new ClaseProgramada(
                Dia.LUNES,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                profesor,
                actividad,
                EstadoClaseProgramada.ACTIVA
        );

        claseProgramadaService.crear(clase);
    }

    @Test
    void crear_inscripcionValida_guardaLaInscripcion() {
        Inscripcion inscripcion = inscripcionService.crear(cliente.getId(), clase.getId());

        assertNotNull(inscripcion);
        assertNotNull(inscripcion.getId());
        assertEquals(cliente.getId(), inscripcion.getCliente().getId());
        assertEquals(clase.getId(), inscripcion.getClaseProgramada().getId());
    }

    /* ERROR, LANZA NullPointerException
    @Test
    void crear_clienteYaInscripto_lanzaExcepcion() {
        inscripcionService.crear(cliente.getId(), clase.getId());

        assertThrows(
                RecursoDuplicadoException.class,
                () -> inscripcionService.crear(cliente.getId(), clase.getId())
        );
    }*/

    @Test
    void crear_claseNoDisponible_lanzaExcepcion() {
        //TODO IMPLEMENTAR CUANDO SE CAMBIEN LOS ESTADOS
    }

    @Test
    void crear_clienteInexistente_lanzaExcepcion() {
        assertThrows(
                RecursoNoEncontradoException.class,
                () -> inscripcionService.crear(-1L, clase.getId())
        );
    }

    @Test
    void crear_claseInexistente_lanzaExcepcion() {
        assertThrows(
                RecursoNoEncontradoException.class,
                () -> inscripcionService.crear(cliente.getId(), -1L)
        );
    }

    @Test
    void recuperar_inscripcionExistente_devuelveInscripcion() {
        Inscripcion creada =
                inscripcionService.crear(cliente.getId(), clase.getId());

        Inscripcion recuperada =
                inscripcionService.recuperar(creada.getId());

        assertNotNull(recuperada);
        assertEquals(creada.getId(), recuperada.getId());
        assertEquals(cliente.getId(), recuperada.getCliente().getId());
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

    /* ERROR CON CLAVE FORÁNEA
    @Test
    void recuperarTodos_devuelveListaConLaInscripcionCreada() {
        Inscripcion creada = inscripcionService.crear(cliente.getId(), clase.getId());

        List<Inscripcion> inscripciones =
                inscripcionService.recuperarTodos();

        assertNotNull(inscripciones);
        assertTrue(
                inscripciones.stream()
                        .anyMatch(i -> i.getId().equals(creada.getId()))
        );
    }*/

    @Test
    void eliminar_inscripcionExistente_eliminaLaInscripcion() {
        Inscripcion creada =
                inscripcionService.crear(cliente.getId(), clase.getId());

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
    }
*/
}