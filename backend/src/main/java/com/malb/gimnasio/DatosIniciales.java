
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
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

    // Guardamos únicamente los identificadores de los datos creados
    // por esta instancia de la aplicación.
    private final List<Long> idsActividades = new ArrayList<>();
    private final List<Long> dnisProfesores = new ArrayList<>();
    private final List<Long> dnisClientes = new ArrayList<>();
    private final List<Long> idsClases = new ArrayList<>();
    private final List<Long> idsInscripciones = new ArrayList<>();

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
        idsActividades.add(musculacion.getId());

        actividadService.crear(funcional);
        idsActividades.add(funcional.getId());

        actividadService.crear(yoga);
        idsActividades.add(yoga.getId());


        // 2. PROFESORES

        Profesor profesor1 = new Profesor(
                30111222L,
                "juan.perez@gimnasio.com",
                "password123",
                "Juan Perez",
                "1123456789",
                "Profesor especializado en entrenamiento de fuerza",
                "Musculación"
        );

        Profesor profesor2 = new Profesor(
                30222333L,
                "ana.garcia@gimnasio.com",
                "password123",
                "Ana Garcia",
                "1134567890",
                "Profesora especializada en entrenamiento funcional",
                "Funcional"
        );

        Profesor profesor3 = new Profesor(
                30333444L,
                "lucas.lopez@gimnasio.com",
                "password123",
                "Lucas Lopez",
                "1145678901",
                "Profesor especializado en yoga",
                "Yoga"
        );

        profesor1.setMisClases(new ArrayList<>());
        profesor2.setMisClases(new ArrayList<>());
        profesor3.setMisClases(new ArrayList<>());

        profesorService.crear(profesor1);
        dnisProfesores.add(profesor1.getDni());

        profesorService.crear(profesor2);
        dnisProfesores.add(profesor2.getDni());

        profesorService.crear(profesor3);
        dnisProfesores.add(profesor3.getDni());


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
        dnisClientes.add(cliente1.getDni());

        clienteService.crear(cliente2);
        dnisClientes.add(cliente2.getDni());

        clienteService.crear(cliente3);
        dnisClientes.add(cliente3.getDni());


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
        idsClases.add(clase1.getId());

        claseProgramadaService.crear(clase2);
        idsClases.add(clase2.getId());

        claseProgramadaService.crear(clase3);
        idsClases.add(clase3.getId());


        // 5. INSCRIPCIONES

        Inscripcion inscripcion1 = new Inscripcion(
                cliente1,
                clase1
        );

        Inscripcion inscripcion2 = new Inscripcion(
                cliente2,
                clase2
        );

        Inscripcion inscripcion3 = new Inscripcion(
                cliente3,
                clase3
        );

        inscripcionService.crear(cliente1, clase1);
        idsInscripciones.add(inscripcion1.getId());

        inscripcionService.crear(cliente2, clase2);
        idsInscripciones.add(inscripcion2.getId());

        inscripcionService.crear(cliente3, clase3);
        idsInscripciones.add(inscripcion3.getId());


        log.info("Datos de prueba cargados correctamente.");
        log.info("Actividades: {}", idsActividades);
        log.info("DNI de profesores: {}", dnisProfesores);
        log.info("DNI de clientes: {}", dnisClientes);
        log.info("Clases programadas: {}", idsClases);
        log.info("Inscripciones: {}", idsInscripciones);
    }

    @PreDestroy
    public void eliminarDatosIniciales() {
        log.info("Eliminando los datos de prueba creados por esta instancia...");

        // 1. Eliminar primero las inscripciones.
        eliminarEnOrdenInverso(
                idsInscripciones,
                id -> inscripcionService.eliminar(id),
                "inscripción"
        );

        // 2. Después, las clases programadas.
        eliminarEnOrdenInverso(
                idsClases,
                id -> claseProgramadaService.eliminar(id),
                "clase programada"
        );

        // 3. Eliminar los clientes por DNI.
        eliminarEnOrdenInverso(
                dnisClientes,
                dni -> clienteService.eliminar(dni),
                "cliente"
        );

        // 4. Eliminar los profesores por DNI.
        eliminarEnOrdenInverso(
                dnisProfesores,
                dni -> profesorService.eliminar(dni),
                "profesor"
        );

        // 5. Por último, las actividades.
        eliminarEnOrdenInverso(
                idsActividades,
                id -> actividadService.eliminar(id),
                "actividad"
        );

        log.info("Finalizó la limpieza de datos de prueba.");
    }

    private void eliminarEnOrdenInverso(
            List<Long> identificadores,
            Eliminador eliminador,
            String tipo
    ) {
        for (int i = identificadores.size() - 1; i >= 0; i--) {
            Long identificador = identificadores.get(i);

            try {
                eliminador.eliminar(identificador);
                log.info("{} eliminado: {}", tipo, identificador);
            } catch (Exception e) {
                log.error(
                        "No se pudo eliminar {} con identificador {}",
                        tipo,
                        identificador,
                        e
                );
            }
        }
    }

    @FunctionalInterface
    private interface Eliminador {
        void eliminar(Long identificador);
    }
}
