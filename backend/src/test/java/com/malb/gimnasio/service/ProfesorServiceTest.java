package com.malb.gimnasio.service;

import com.malb.gimnasio.modelo.usuario.Profesor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProfesorServiceTest {

    @Autowired
    private ProfesorService profesorService;

    @Test
    void crear() {

        Profesor profesor = new Profesor(60111222L, "profesor@test.com", "Profesor Test", "1100000010", "Profesor de prueba", "Musculación");

        profesor.setMisClases(new ArrayList<>());

        profesorService.crear(profesor);

        assertNotNull(profesor.getDni());
    }

    @Test
    void recuperar() {

        Profesor profesor = new Profesor(60222333L, "profesor2@test.com", "Profesor Test 2", "1100000011", "Profesor de prueba", "Yoga");

        profesor.setMisClases(new ArrayList<>());

        profesorService.crear(profesor);

        Profesor recuperado = profesorService.recuperar(profesor.getId());

        assertNotNull(recuperado);
        assertEquals(profesor.getDni(), recuperado.getDni());
        assertEquals("Profesor Test 2", recuperado.getNombre());
    }

    @Test
    void recuperarTodos() {

        List<Profesor> profesores = profesorService.recuperarTodos();

        assertNotNull(profesores);
    }

    @Test
    void eliminar() {

        Profesor profesor = new Profesor(60333444L, "profesor3@test.com", "Profesor Test 3", "1100000012", "Profesor de prueba", "Funcional");

        profesor.setMisClases(new ArrayList<>());

        profesorService.crear(profesor);

        Long id = profesor.getId();

        profesorService.eliminar(id);

        assertThrows(RuntimeException.class, () -> profesorService.recuperar(id));
    }
    @AfterEach
    public void tearDown() {
        profesorService.eliminarTodos();
    }
}