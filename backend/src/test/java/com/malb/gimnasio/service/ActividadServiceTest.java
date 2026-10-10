package com.malb.gimnasio.service;

import com.malb.gimnasio.modelo.Actividad;
import com.malb.gimnasio.service.ActividadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ActividadServiceTest {

    @Autowired
    private ActividadService actividadService;

    @Test
    void crear() {

        Actividad actividad = new Actividad("Musculación", "Entrenamiento de fuerza", 20);

        actividadService.crear(actividad);

        assertNotNull(actividad.getId());
    }

    @Test
    void recuperar() {

        Actividad actividad = new Actividad("Yoga", "Clase de yoga", 15);

        actividadService.crear(actividad);

        Actividad recuperada = actividadService.recuperar(actividad.getId());

        assertNotNull(recuperada);
        assertEquals(actividad.getId(), recuperada.getId());
        assertEquals("Yoga", recuperada.getNombre());
    }

    @Test
    void recuperarTodos() {

        List<Actividad> actividades = actividadService.recuperarTodos();

        assertNotNull(actividades);
    }

    @Test
    void eliminar() {

        Actividad actividad = new Actividad("Funcional", "Entrenamiento funcional", 15);

        actividadService.crear(actividad);

        Long id = actividad.getId();

        actividadService.eliminar(id);

        assertThrows(RuntimeException.class, () -> actividadService.recuperar(id));
    }
}