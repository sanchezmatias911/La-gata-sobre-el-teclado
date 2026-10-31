package com.malb.gimnasio.modelo;

import com.malb.gimnasio.modelo.usuario.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InscripcionTest {
    Cliente cliente;
    ClaseProgramada clase0, clase1, clase2;
    Inscripcion inscripcion;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        clase0  = new ClaseProgramada();
        clase1  = new ClaseProgramada();
        clase2  = new ClaseProgramada();

        inscripcion = new Inscripcion(cliente, clase0);
    }

    // ---------------------------------------------------------------
    // Casos buenos
    // ---------------------------------------------------------------

    @Test
    void creaConClienteYClase() {
        assertEquals(cliente, inscripcion.getCliente());
        assertEquals(clase0, inscripcion.getClaseProgramada());
        assertNotNull(inscripcion.getFechaDeInscripcion());
    }


    @Test
    void unClientePuedeTenerVariasInscripciones() {

        Inscripcion primera = new Inscripcion(cliente, clase1);
        Inscripcion segunda = new Inscripcion(cliente, clase2);

        assertEquals(cliente, primera.getCliente());
        assertEquals(cliente, segunda.getCliente());
    }

    // ---------------------------------------------------------------
    // Casos malos
    // ---------------------------------------------------------------

    @Test
    void noSePuedeCrearSinCliente() {
        assertThrows(
                NullPointerException.class,
                () -> new Inscripcion(null, clase0)
        );
    }

    @Test
    void noSePuedeCrearSinClase() {
        assertThrows(
                NullPointerException.class,
                () -> new Inscripcion(cliente, null)
        );
    }
}
