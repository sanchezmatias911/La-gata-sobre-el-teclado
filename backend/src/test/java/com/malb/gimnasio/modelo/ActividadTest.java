package com.malb.gimnasio.modelo;

import com.malb.gimnasio.modelo.exception.CupoInvalidoException;
import com.malb.gimnasio.modelo.exception.RecursoInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ActividadTest {
    Actividad actividad;


    @BeforeEach
    void setUp() {
        actividad = new Actividad("Musculación", "Entrenamiento de fuerza", 20);
    }

    // ---------------------------------------------------------------
    // Casos buenos
    // ---------------------------------------------------------------

    @Test
    void creaConDatosValidos() {
        assertEquals("Musculación", actividad.getNombre());
        assertEquals("Entrenamiento de fuerza", actividad.getDescripcion());
        assertEquals(20, actividad.getCupo());
    }

    // ---------------------------------------------------------------
    // Casos malos
    // ---------------------------------------------------------------

    @Test
    void noSePuedeCrearSinNombre() {
        assertThrows(NullPointerException.class,
                () -> new Actividad(null, "Clase de yoga", 15)
        );
    }

    @Test
    void rechazaCupoInvalidoAsignadoPorSetter() {
        assertThrows(CupoInvalidoException.class,
                () -> actividad.setCupo(51)
        );
    }

    // Nombre vacío o con espacios
    @Test
    void rechazaNombreEnBlanco() {
        assertThrows(
                RecursoInvalidoException.class,
                () -> new Actividad("   ", "Descripción válida", 10)
        );
    }

    // Descripción vacía
    @Test
    void rechazaDescripcionEnBlanco() {
        assertThrows(
                RecursoInvalidoException.class,
                () -> new Actividad("Yoga", "   ", 10)
        );
    }

    // Cupo igual a cero
    @Test
    void rechazaCupoCero() {
        assertThrows(
                CupoInvalidoException.class,
                () -> new Actividad("Yoga", "Descripción válida", 0)
        );
    }

    // Cupo negativo
    @Test
    void rechazaCupoNegativo() {
        assertThrows(
                CupoInvalidoException.class,
                () -> new Actividad("Yoga", "Descripción válida", -1)
        );
    }

    // Setter de nombre
    @Test
    void setterRechazaNombreEnBlanco() {
        assertThrows(
                RecursoInvalidoException.class,
                () -> actividad.setNombre(" ")
        );
    }

    // Setter de descripción
    @Test
    void setterRechazaDescripcionEnBlanco() {
        assertThrows(
                RecursoInvalidoException.class,
                () -> actividad.setDescripcion("")
        );
    }

    // Setter de cupo
    @Test
    void setterRechazaCupoNegativo() {
        assertThrows(
                CupoInvalidoException.class,
                () -> actividad.setCupo(-1)
        );
    }

    // Límites válidos
    @Test
    void aceptaCupoMinimoYMaximo() {
        actividad.setCupo(1);
        assertEquals(1, actividad.getCupo());

        actividad.setCupo(50);
        assertEquals(50, actividad.getCupo());
    }

}
