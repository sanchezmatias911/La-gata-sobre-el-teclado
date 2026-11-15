
package com.malb.gimnasio;

import com.malb.gimnasio.dao.UsuarioDAO;
import com.malb.gimnasio.modelo.Actividad;
import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Dia;
import com.malb.gimnasio.modelo.EstadoClaseProgramada;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.usuario.Administrador;
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
    private final UsuarioDAO usuarioDAO;
    private final PasswordEncoder passwordEncoder;

    // Credenciales del administrador de DESARROLLO. Están documentadas en
    // ReadmeDatosIniciales.md para que el frontend pueda iniciar sesión.
    // Esta clase solo corre con cargar.datos.dummy=true: nunca en producción.
    private static final String ADMIN_EMAIL = "admin@gimnasio.com";
    private static final String ADMIN_PASSWORD = "Admin123456";
    private static final Long ADMIN_DNI = 20111222L;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Cargando datos iniciales de prueba...");

        // 0. ADMINISTRADOR (único usuario que puede registrar clases, profesores y actividades)

        crearAdministradorDeDesarrollo();

        // 1. ACTIVIDADES

        Actividad funcional = new Actividad(
                "Funcional",
                """
                    Desarrollá fuerza, agilidad y resistencia con ejercicios que imitan movimientos cotidianos.
                    Clase dinámica con pesas, sogas y peso corporal para quemar calorías y tonificar todo el cuerpo,
                    mejorando tu condición física sin importar tu nivel.
                """,
                15
        );

        Actividad fitcombat = new Actividad(
                "Fit Combat",
                """
                    Disciplina de alta intensidad que combina técnicas de boxeo y muay thai con coreografías
                    musicales. Quemá calorías mientras ganás potencia, velocidad y resistencia cardiovascular
                    en un ambiente motivador. ¡Golpeá, pateá y superá tus límites!
                """,
                20
        );

        Actividad yoga = new Actividad(
                "Yoga",
                """
                    Encontrá tu equilibrio y mejorá tu flexibilidad mediante posturas, respiración consciente y
                    meditación. Ideal para reducir estrés, corregir la postura, aliviar tensiones y recargar
                    energías. Un espacio de paz apto para todos los niveles.
                """,
                25
        );

        Actividad stretching = new Actividad(
                "Stretching",
                """
                    Clase enfocada en la elongación profunda, liberación miofascial y corrección postural.
                    Complemento perfecto para cualquier rutina: previene lesiones, disminuye la rigidez corporal,
                    mejora el rango de movimiento y brinda bienestar absoluto.
                """,
                20
        );

        Actividad zumba = new Actividad(
                "Zumba",
                """
                    Combiná pasos de baile latino e internacional con ritmos aeróbicos para una sesión de cardio
                    muy divertida. Tonificá piernas, abdomen y glúteos mientras mejorás tu coordinación y liberás
                    endorfinas. ¡No se requiere experiencia previa!
                """,
                15
        );

        Actividad fulllocal = new Actividad(
                "Full Local",
                """
                    Clase diseñada para trabajar de forma localizada piernas, glúteos y abdomen. Con series
                    dinámicas usando mancuernas, bandas y tu peso corporal, vas a quemar calorías, mejorar tu
                    resistencia y definir cada músculo al ritmo de la música.
                """,
                20
        );

        actividadService.crear(funcional);
        actividadService.crear(fitcombat);
        actividadService.crear(yoga);
        actividadService.crear(stretching);
        actividadService.crear(zumba);
        actividadService.crear(fulllocal);

        // 2. PROFESORES

        Profesor profesor1 = new Profesor(
                30111222L,
                "juan.perez@gimnasio.com",
                "Juan Perez",
                "1123456789",
                "Profesor especializado en entrenamiento Funcional",
                "Funcional"
        );

        Profesor profesor2 = new Profesor(
                30222333L,
                "ana.garcia@gimnasio.com",
                "Ana Garcia",
                "1134567890",
                "Profesora especializada en Fit Combat",
                "Fit Combat"
        );

        Profesor profesor3 = new Profesor(
                30333444L,
                "lucas.lopez@gimnasio.com",
                "Lucas Lopez",
                "1145678901",
                "Profesor especializado en Yoga",
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
                "password123",
                "Maria Gonzalez",
                "1156789012"
        );

        Cliente cliente2 = new Cliente(
                40222333L,
                "pedro.rodriguez@gmail.com",
                "password123",
                "Pedro Rodriguez",
                "1167890123"
        );

        Cliente cliente3 = new Cliente(
                40333444L,
                "sofia.martinez@gmail.com",
                "password123",
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
                LocalTime.of(9, 0),
                LocalTime.of(10, 30),
                profesor1,
                funcional,
                EstadoClaseProgramada.ACTIVA
        );

        ClaseProgramada clase2 = new ClaseProgramada(
                Dia.MIERCOLES,
                LocalTime.of(12, 0),
                LocalTime.of(15, 0),
                profesor2,
                fitcombat,
                EstadoClaseProgramada.ACTIVA
        );

        ClaseProgramada clase3 = new ClaseProgramada(
                Dia.VIERNES,
                LocalTime.of(19, 30),
                LocalTime.of(21, 0),
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

    private void crearAdministradorDeDesarrollo() {
        if (usuarioDAO.existsByEmailIgnoreCase(ADMIN_EMAIL)) {
            log.info("El administrador de desarrollo ya existe. No se crea otro.");
            return;
        }

        usuarioDAO.save(new Administrador(
                ADMIN_DNI,
                ADMIN_EMAIL,
                passwordEncoder.encode(ADMIN_PASSWORD),
                "Administrador",
                "1100000000"
        ));

        log.info("Administrador de desarrollo creado: {}", ADMIN_EMAIL);
    }

    // El administrador NO se borra acá: puede venir de ADMIN_EMAIL/ADMIN_PASSWORD
    // y tiene que sobrevivir al apagado. Si ya existe, no se vuelve a crear.
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