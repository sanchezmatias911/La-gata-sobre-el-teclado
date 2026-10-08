package com.malb.gimnasio;

import com.malb.gimnasio.modelo.Actividad;
import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Dia;
import com.malb.gimnasio.modelo.EstadoClaseProgramada;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.modelo.usuario.Profesor;

import com.malb.gimnasio.service.ActividadService;
import com.malb.gimnasio.service.ClaseProgramadaService;
import com.malb.gimnasio.service.ClienteService;
import com.malb.gimnasio.service.InscripcionService;
import com.malb.gimnasio.service.ProfesorService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.ArrayList;

@SpringBootTest
public class CargarDatosTest {

    @Autowired
    private ActividadService actividadService;

    @Autowired
    private ProfesorService profesorService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClaseProgramadaService claseProgramadaService;

    @Autowired
    private InscripcionService inscripcionService;


    @Test
    void cargarDatos() {

        // =========================================================
        // 1. ACTIVIDADES
        // =========================================================

        Actividad musculacion = new Actividad("Musculación", "Entrenamiento de fuerza", 20);

        Actividad funcional = new Actividad("Funcional", "Entrenamiento funcional de alta intensidad", 15);

        Actividad yoga = new Actividad("Yoga", "Clase de yoga y relajación", 10);

        actividadService.crear(musculacion);
        actividadService.crear(funcional);
        actividadService.crear(yoga);


        // =========================================================
        // 2. PROFESORES
        // =========================================================

        Profesor profesor1 = new Profesor(30111222L, "juan.perez@gimnasio.com", "password123", "Juan Perez", "1123456789", "Profesor especializado en entrenamiento de fuerza", "Musculación");

        profesor1.setMisClases(new ArrayList<>());


        Profesor profesor2 = new Profesor(30222333L, "ana.garcia@gimnasio.com", "password123", "Ana Garcia", "1134567890", "Profesora especializada en entrenamiento funcional", "Funcional");

        profesor2.setMisClases(new ArrayList<>());


        Profesor profesor3 = new Profesor(30333444L, "lucas.lopez@gimnasio.com", "password123", "Lucas Lopez", "1145678901", "Profesor especializado en yoga", "Yoga");

        profesor3.setMisClases(new ArrayList<>());


        profesorService.crear(profesor1);
        profesorService.crear(profesor2);
        profesorService.crear(profesor3);


        // =========================================================
        // 3. CLIENTES
        // =========================================================

        Cliente cliente1 = new Cliente(40111222L, "maria.gonzalez@gmail.com", "password123", "Maria Gonzalez", "1156789012");

        cliente1.setMisInscripciones(new ArrayList<>());


        Cliente cliente2 = new Cliente(40222333L, "pedro.rodriguez@gmail.com", "password123", "Pedro Rodriguez", "1167890123");

        cliente2.setMisInscripciones(new ArrayList<>());


        Cliente cliente3 = new Cliente(40333444L, "sofia.martinez@gmail.com", "password123", "Sofia Martinez", "1178901234");

        cliente3.setMisInscripciones(new ArrayList<>());


        clienteService.crear(cliente1);
        clienteService.crear(cliente2);
        clienteService.crear(cliente3);


        // =========================================================
        // 4. CLASES PROGRAMADAS
        // =========================================================

        ClaseProgramada clase1 = new ClaseProgramada(Dia.LUNES, LocalTime.of(18, 0), LocalTime.of(19, 0), profesor1, musculacion, EstadoClaseProgramada.ACTIVA);

        clase1.setInscripciones(new ArrayList<>());


        ClaseProgramada clase2 = new ClaseProgramada(Dia.MIERCOLES, LocalTime.of(19, 0), LocalTime.of(20, 0), profesor2, funcional, EstadoClaseProgramada.ACTIVA);

        clase2.setInscripciones(new ArrayList<>());


        ClaseProgramada clase3 = new ClaseProgramada(Dia.VIERNES, LocalTime.of(17, 0), LocalTime.of(18, 0), profesor3, yoga, EstadoClaseProgramada.ACTIVA);

        clase3.setInscripciones(new ArrayList<>());


        claseProgramadaService.crear(clase1);
        claseProgramadaService.crear(clase2);
        claseProgramadaService.crear(clase3);


        // =========================================================
        // 5. INSCRIPCIONES
        // =========================================================

        Inscripcion inscripcion1 = new Inscripcion(cliente1, clase1);

        Inscripcion inscripcion2 = new Inscripcion(cliente2, clase2);

        Inscripcion inscripcion3 = new Inscripcion(cliente3, clase3);


        inscripcionService.crear(inscripcion1);
        inscripcionService.crear(inscripcion2);
        inscripcionService.crear(inscripcion3);


        // =========================================================
        // 6. MOSTRAR LOS IDs
        // =========================================================

        System.out.println();
        System.out.println("======================================");
        System.out.println("       DATOS CARGADOS CORRECTAMENTE");
        System.out.println("======================================");

        System.out.println();
        System.out.println("ACTIVIDADES");
        System.out.println("Musculación: " + musculacion.getId());
        System.out.println("Funcional:   " + funcional.getId());
        System.out.println("Yoga:        " + yoga.getId());

        System.out.println();
        System.out.println("PROFESORES");
        System.out.println("Juan:  " + profesor1.getDni());
        System.out.println("Ana:   " + profesor2.getDni());
        System.out.println("Lucas: " + profesor3.getDni());

        System.out.println();
        System.out.println("CLIENTES");
        System.out.println("Maria: " + cliente1.getDni());
        System.out.println("Pedro: " + cliente2.getDni());
        System.out.println("Sofia: " + cliente3.getDni());

        System.out.println();
        System.out.println("CLASES");
        System.out.println("Clase 1: " + clase1.getId());
        System.out.println("Clase 2: " + clase2.getId());
        System.out.println("Clase 3: " + clase3.getId());

        System.out.println();
        System.out.println("INSCRIPCIONES");
        System.out.println("Inscripción 1: " + inscripcion1.getId());
        System.out.println("Inscripción 2: " + inscripcion2.getId());
        System.out.println("Inscripción 3: " + inscripcion3.getId());

        System.out.println();
        System.out.println("======================================");
    }
}