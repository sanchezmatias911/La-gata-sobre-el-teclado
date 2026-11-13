package com.malb.gimnasio.modelo;

import com.malb.gimnasio.modelo.exception.HorarioInvalido;
import com.malb.gimnasio.modelo.exception.InscripcionNoPermitidaException;
import com.malb.gimnasio.modelo.usuario.Profesor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static com.malb.gimnasio.modelo.EstadoClaseProgramada.LLENA;
import static org.junit.jupiter.api.Assertions.*;

public class ClaseProgramadaTest {
    private static final LocalTime INICIO = LocalTime.of(18, 0);
    private static final LocalTime FIN = LocalTime.of(19, 0);

    Profesor profesor;
    Actividad actividad;
    ClaseProgramada clase;

    @BeforeEach
    void setUp() {
        profesor = new Profesor();
        actividad = new Actividad("Musculación", "Entrenamiento de fuerza", 20);
        clase = new ClaseProgramada(Dia.MARTES, INICIO, FIN, profesor, actividad, EstadoClaseProgramada.ACTIVA);
    }

    // ---------------------------------------------------------------
    // Casos buenos
    // ---------------------------------------------------------------

    @Test
    void creaConDatosValidos() {
        assertEquals(Dia.MARTES, clase.getDia());
        assertEquals(INICIO, clase.getHoraInicio());
        assertEquals(FIN, clase.getHoraFin());
        assertEquals(EstadoClaseProgramada.ACTIVA, clase.getEstado());
        assertEquals(0, clase.cantidadInscriptos());
    }

    @Test
    void permiteCambiarElEstado() {
        clase.setEstado(EstadoClaseProgramada.INACTIVA);

        assertEquals(EstadoClaseProgramada.INACTIVA, clase.getEstado());
    }

    @Test
    void seIncrementaLosInsriptosCuandoAguienSeAnota() {
        clase.agregarInscripcion(new Inscripcion());

        assertEquals(1, clase.cantidadInscriptos());
    }

    // ---------------------------------------------------------------
    // Casos malos
    // ---------------------------------------------------------------

    @Test
    void noSePuedeAnotarALaClaseCuandoEstaLlena() {
        Actividad actividad1Cupo = new Actividad("Musculación", "Entrenamiento de fuerza", 1);
        ClaseProgramada clase1Cupo = new ClaseProgramada(Dia.MARTES, INICIO, FIN, profesor, actividad1Cupo, EstadoClaseProgramada.ACTIVA);

        clase1Cupo.agregarInscripcion(new Inscripcion());

        assertEquals(LLENA, clase1Cupo.getEstado());
        assertThrows(InscripcionNoPermitidaException.class, () ->
                clase1Cupo.agregarInscripcion(new Inscripcion())
        );
    }

    @Test
    void rechazaHorariosInvalidos() {
        assertThrows(HorarioInvalido.class, () ->
                new ClaseProgramada(Dia.LUNES, FIN, INICIO, profesor, actividad, EstadoClaseProgramada.ACTIVA)
        );
    }

    @Test
    void rechazaArgumentosNulos() {
        EstadoClaseProgramada estado = EstadoClaseProgramada.ACTIVA;

        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> new ClaseProgramada(null, INICIO, FIN, profesor, actividad, estado), "dia"),
                () -> assertThrows(NullPointerException.class,
                        () -> new ClaseProgramada(Dia.LUNES, null, FIN, profesor, actividad, estado), "horaInicio"),
                () -> assertThrows(NullPointerException.class,
                        () -> new ClaseProgramada(Dia.LUNES, INICIO, null, profesor, actividad, estado), "horaFin"),
                () -> assertThrows(NullPointerException.class,
                        () -> new ClaseProgramada(Dia.LUNES, INICIO, FIN, null, actividad, estado), "profesor"),
                () -> assertThrows(NullPointerException.class,
                        () -> new ClaseProgramada(Dia.LUNES, INICIO, FIN, profesor, null, estado), "actividad"),
                () -> assertThrows(NullPointerException.class,
                        () -> new ClaseProgramada(Dia.LUNES, INICIO, FIN, profesor, actividad, null), "estado")
        );
    }

    @Test
    void crearClaseConHorarioDentroDelRango() {
        assertDoesNotThrow(() ->
                new ClaseProgramada(
                        Dia.LUNES,
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0),
                        profesor,
                        actividad,
                        EstadoClaseProgramada.ACTIVA
                )
        );
    }

    @Test
    void crearClaseQueEmpiezaAntesDeLas8() {
        assertThrows(HorarioInvalido.class, () ->
                new ClaseProgramada(
                        Dia.LUNES,
                        LocalTime.of(7, 59),
                        LocalTime.of(9, 0),
                        profesor,
                        actividad,
                        EstadoClaseProgramada.ACTIVA
                )
        );
    }

    @Test
    void crearClaseQueTerminaDespuesDeLas21() {
        assertThrows(HorarioInvalido.class, () ->
                new ClaseProgramada(
                        Dia.LUNES,
                        LocalTime.of(19, 0),
                        LocalTime.of(21, 1),
                        profesor,
                        actividad,
                        EstadoClaseProgramada.ACTIVA
                )
        );
    }

    @Test
    void crearClaseQueEmpiezaExactamenteALas8() {
        assertDoesNotThrow(() ->
                new ClaseProgramada(
                        Dia.LUNES,
                        LocalTime.of(8, 0),
                        LocalTime.of(9, 0),
                        profesor,
                        actividad,
                        EstadoClaseProgramada.ACTIVA
                )
        );
    }

    @Test
    void crearClaseQueTerminaExactamenteALas21() {
        assertDoesNotThrow(() ->
                new ClaseProgramada(
                        Dia.LUNES,
                        LocalTime.of(19, 0),
                        LocalTime.of(21, 0),
                        profesor,
                        actividad,
                        EstadoClaseProgramada.ACTIVA
                )
        );
    }

}
