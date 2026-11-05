
package com.malb.gimnasio;

import com.malb.gimnasio.modelo.Actividad;
import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Dia;
import com.malb.gimnasio.modelo.EstadoClaseProgramada;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.modelo.usuario.Profesor;
import com.malb.gimnasio.service.*;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "cargar.datos.dummy",
        havingValue = "true"
)
public class DatosIniciales implements ApplicationRunner {

    private final ActividadService actividadService;
    private final ProfesorService profesorService;
    private final ClienteService clienteService;
    private final ClaseProgramadaService claseProgramadaService;
    private final InscripcionService inscripcionService;
    private final LimpiezaBaseDeDatosService limpiezaBaseDeDatosService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Cargando datos iniciales de prueba...");

        // 1. ACTIVIDADES

        Actividad musculacion = new Actividad(
                "Musculación",
                "Entrenamiento de fuerza",
                20
        );

        Actividad funcional = new Actividad(
                "Funcional",
                "Entrenamiento funcional de alta intensidad",
                15
        );

        Actividad yoga = new Actividad(
                "Yoga",
                "Clase de yoga y relajación",
                10
        );

        actividadService.crear(musculacion);
        actividadService.crear(funcional);
        actividadService.crear(yoga);

        // 2. PROFESORES

        Profesor profesor1 = new Profesor(
                30111222L,
                "juan.perez@gimnasio.com",
                "Juan Perez",
                "1123456789",
                "Profesor especializado en entrenamiento de fuerza",
                "Musculación"
        );

        Profesor profesor2 = new Profesor(
                30222333L,
                "ana.garcia@gimnasio.com",
                "Ana Garcia",
                "1134567890",
                "Profesora especializada en entrenamiento funcional",
                "Funcional"
        );

        Profesor profesor3 = new Profesor(
                30333444L,
                "lucas.lopez@gimnasio.com",
                "Lucas Lopez",
                "1145678901",
                "Profesor especializado en yoga",
                "Yoga"
        );

        profesor1.setMisClases(new ArrayList<>());
        profesor2.setMisClases(new ArrayList<>());
        profesor3.setMisClases(new ArrayList<>());

        profesorService.crear(profesor1);
        profesorService.crear(profesor2);
        profesorService.crear(profesor3);

        // 3. CLIENTES

        Cliente cliente1 = new Cliente(
                40111222L,
                "maria.gonzalez@gmail.com",
                passwordEncoder.encode("password123"),
                "Maria Gonzalez",
                "1156789012"
        );

        Cliente cliente2 = new Cliente(
                40222333L,
                "pedro.rodriguez@gmail.com",
                passwordEncoder.encode("password123"),
                "Pedro Rodriguez",
                "1167890123"
        );

        Cliente cliente3 = new Cliente(
                40333444L,
                "sofia.martinez@gmail.com",
                passwordEncoder.encode("password123"),
                "Sofia Martinez",
                "1178901234"
        );

        cliente1.setMisInscripciones(new ArrayList<>());
        cliente2.setMisInscripciones(new ArrayList<>());
        cliente3.setMisInscripciones(new ArrayList<>());

        clienteService.crear(cliente1);
        clienteService.crear(cliente2);
        clienteService.crear(cliente3);

        // 4. CLASES PROGRAMADAS
        // Los horarios no se superponen en el mismo día.

        ClaseProgramada clase1 = new ClaseProgramada(
                Dia.LUNES,
                LocalTime.of(18, 0),
                LocalTime.of(19, 0),
                profesor1,
                musculacion,
                EstadoClaseProgramada.ACTIVA
        );

        ClaseProgramada clase2 = new ClaseProgramada(
                Dia.MIERCOLES,
                LocalTime.of(19, 0),
                LocalTime.of(20, 0),
                profesor2,
                funcional,
                EstadoClaseProgramada.ACTIVA
        );

        ClaseProgramada clase3 = new ClaseProgramada(
                Dia.VIERNES,
                LocalTime.of(17, 0),
                LocalTime.of(18, 0),
                profesor3,
                yoga,
                EstadoClaseProgramada.ACTIVA
        );

        clase1.setInscripciones(new ArrayList<>());
        clase2.setInscripciones(new ArrayList<>());
        clase3.setInscripciones(new ArrayList<>());

        claseProgramadaService.crear(clase1);
        claseProgramadaService.crear(clase2);
        claseProgramadaService.crear(clase3);

        // 5. INSCRIPCIONES

        Inscripcion inscripcion1 = inscripcionService.crear(cliente1, clase1);
        Inscripcion inscripcion2 = inscripcionService.crear(cliente2, clase2);
        Inscripcion inscripcion3 = inscripcionService.crear(cliente3, clase3);

        log.info("Datos de prueba cargados correctamente.");
    }

    @PreDestroy
    public void eliminarDatosIniciales() {
        log.info("Solicitando limpieza de la base de datos al cerrar la aplicación...");

        try {
            limpiezaBaseDeDatosService.limpiarBaseDeDatos();
            log.info("Base de datos limpiada correctamente.");
        } catch (Exception e) {
            log.error("No se pudo limpiar la base de datos al cerrar.", e);
        }
    }
}
