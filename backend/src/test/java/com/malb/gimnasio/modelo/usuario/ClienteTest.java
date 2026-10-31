package com.malb.gimnasio.modelo.usuario;

import com.malb.gimnasio.modelo.*;
import com.malb.gimnasio.modelo.exception.ConflictoHorarioException;
import com.malb.gimnasio.modelo.exception.RecursoDuplicadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteTest {
    private static final LocalTime DIEZ = LocalTime.of(10, 0);
    private static final LocalTime ONCE = LocalTime.of(11, 0);
    private static final LocalTime DOCE = LocalTime.of(12, 0);

    Cliente cliente;
    Profesor profesor;
    Actividad actividad;

    ClaseProgramada clase;
    ClaseProgramada otraClase;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(
                50111222L,
                "cliente@test.com",
                "password123",
                "Cliente Test",
                "1100000000"
        );

        profesor = new Profesor();

        actividad = new Actividad(
                "Musculación",
                "Entrenamiento de fuerza",
                20
        );

        clase = new ClaseProgramada(
                Dia.LUNES,
                DIEZ,
                ONCE,
                profesor,
                actividad,
                EstadoClaseProgramada.ACTIVA
        );

        clase.setId(1L);

        otraClase = new ClaseProgramada(
                Dia.LUNES,
                DOCE,
                LocalTime.of(13, 0),
                profesor,
                actividad,
                EstadoClaseProgramada.ACTIVA
        );

        otraClase.setId(2L);
    }

    // ---------------------------------------------------------------
    // Casos buenos
    // ---------------------------------------------------------------

    @Test
    void creaConDatosValidos() {
        assertEquals(50111222L, cliente.getDni());
        assertEquals("cliente@test.com", cliente.getEmail());
        assertEquals("password123", cliente.getPassword());
        assertEquals("Cliente Test", cliente.getNombre());
        assertEquals("1100000000", cliente.getTelefono());

        assertNotNull(cliente.getFechaDeRegistro());
        assertNotNull(cliente.getMisInscripciones());
        assertTrue(cliente.getMisInscripciones().isEmpty());
    }

    @Test
    void asignaLaFechaDeRegistroAlMomentoDeCreacion() {
        LocalDateTime antesDeCrear = LocalDateTime.now();

        Cliente nuevoCliente = new Cliente(
                50111223L,
                "nuevo@test.com",
                "password123",
                "Nuevo Cliente",
                "1100000001"
        );

        LocalDateTime despuesDeCrear = LocalDateTime.now();

        assertNotNull(nuevoCliente.getFechaDeRegistro());
        assertFalse(nuevoCliente.getFechaDeRegistro().isBefore(antesDeCrear));
        assertFalse(nuevoCliente.getFechaDeRegistro().isAfter(despuesDeCrear));
    }

    @Test
    void agregaUnaInscripcion() {
        Inscripcion inscripcion = new Inscripcion(cliente, clase);

        cliente.agregarInscripcion(inscripcion);

        assertEquals(1, cliente.getMisInscripciones().size());
        assertTrue(cliente.getMisInscripciones().contains(inscripcion));
    }

    @Test
    void unClientePuedeTenerVariasInscripciones() {
        Inscripcion primera = new Inscripcion(cliente, clase);
        Inscripcion segunda = new Inscripcion(cliente, otraClase);

        cliente.agregarInscripcion(primera);
        cliente.agregarInscripcion(segunda);

        assertEquals(2, cliente.getMisInscripciones().size());
        assertTrue(cliente.getMisInscripciones().contains(primera));
        assertTrue(cliente.getMisInscripciones().contains(segunda));
    }

    @Test
    void permiteInscribirseEnClasesDeDistintosDias() {
        ClaseProgramada claseMartes = new ClaseProgramada(
                Dia.MARTES,
                DIEZ,
                ONCE,
                profesor,
                actividad,
                EstadoClaseProgramada.ACTIVA
        );
        claseMartes.setId(3L);

        cliente.agregarInscripcion(new Inscripcion(cliente, clase));
        cliente.agregarInscripcion(new Inscripcion(cliente, claseMartes));

        assertEquals(2, cliente.getMisInscripciones().size());
    }

    @Test
    void permiteInscribirseEnClasesConsecutivas() {
        ClaseProgramada claseConsecutiva = new ClaseProgramada(
                Dia.LUNES,
                ONCE,
                DOCE,
                profesor,
                actividad,
                EstadoClaseProgramada.ACTIVA
        );
        claseConsecutiva.setId(3L);

        cliente.agregarInscripcion(new Inscripcion(cliente, clase));
        cliente.agregarInscripcion(new Inscripcion(cliente, claseConsecutiva));

        assertEquals(2, cliente.getMisInscripciones().size());
    }

    // ---------------------------------------------------------------
    // Casos malos
    // ---------------------------------------------------------------

    @Test
    void noSePuedeCrearSinDatosObligatorios() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> new Cliente(
                                null, "cliente@test.com", "password123",
                                "Cliente Test", "1100000000"
                        ), "dni"),

                () -> assertThrows(NullPointerException.class,
                        () -> new Cliente(
                                50111222L, null, "password123",
                                "Cliente Test", "1100000000"
                        ), "email"),

                () -> assertThrows(NullPointerException.class,
                        () -> new Cliente(
                                50111222L, "cliente@test.com", null,
                                "Cliente Test", "1100000000"
                        ), "password"),

                () -> assertThrows(NullPointerException.class,
                        () -> new Cliente(
                                50111222L, "cliente@test.com", "password123",
                                null, "1100000000"
                        ), "nombre"),

                () -> assertThrows(NullPointerException.class,
                        () -> new Cliente(
                                50111222L, "cliente@test.com", "password123",
                                "Cliente Test", null
                        ), "telefono")
        );
    }

    @Test
    void noSePuedeInscribirDosVecesEnLaMismaClase() {
        Inscripcion primera = new Inscripcion(cliente, clase);
        Inscripcion segunda = new Inscripcion(cliente, clase);

        cliente.agregarInscripcion(primera);

        assertThrows(
                RecursoDuplicadoException.class,
                () -> cliente.agregarInscripcion(segunda)
        );

        assertEquals(1, cliente.getMisInscripciones().size());
    }

    @Test
    void noSePuedeInscribirEnClasesConHorariosSuperpuestos() {
        ClaseProgramada claseSuperpuesta = new ClaseProgramada(
                Dia.LUNES,
                LocalTime.of(10, 30),
                LocalTime.of(11, 30),
                profesor,
                actividad,
                EstadoClaseProgramada.ACTIVA
        );
        claseSuperpuesta.setId(3L);

        cliente.agregarInscripcion(new Inscripcion(cliente, clase));

        assertThrows(
                ConflictoHorarioException.class,
                () -> cliente.agregarInscripcion(
                        new Inscripcion(cliente, claseSuperpuesta)
                )
        );

        assertEquals(1, cliente.getMisInscripciones().size());
    }

    @Test
    void noSePuedeInscribirEnUnaClaseQueContieneElHorarioDeOtra() {
        ClaseProgramada claseMasLarga = new ClaseProgramada(
                Dia.LUNES,
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                profesor,
                actividad,
                EstadoClaseProgramada.ACTIVA
        );
        claseMasLarga.setId(3L);

        cliente.agregarInscripcion(new Inscripcion(cliente, clase));

        assertThrows(
                ConflictoHorarioException.class,
                () -> cliente.agregarInscripcion(
                        new Inscripcion(cliente, claseMasLarga)
                )
        );
    }
}
