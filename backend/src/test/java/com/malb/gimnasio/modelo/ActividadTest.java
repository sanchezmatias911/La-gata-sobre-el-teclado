package com.malb.gimnasio.modelo;

import com.malb.gimnasio.modelo.exception.CupoInvalidoException;
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
}
