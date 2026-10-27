package com.malb.gimnasio.modelo;

import com.malb.gimnasio.modelo.usuario.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

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

    }


    @Test
    void unClientePuedeTenerVariasInscripciones() {

        Inscripcion primera = new Inscripcion(cliente, clase1);
        Inscripcion segunda = new Inscripcion(cliente, clase2);

    }
}
