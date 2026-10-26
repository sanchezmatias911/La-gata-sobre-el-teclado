package com.malb.gimnasio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.malb.gimnasio.dao.ActividadDAO;
import com.malb.gimnasio.dao.ClaseProgramadaDAO;
import com.malb.gimnasio.dao.ClienteDAO;
import com.malb.gimnasio.dao.InscripcionDAO;
import com.malb.gimnasio.dao.ProfesorDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// ============================================================================
// ¿QUÉ ES UN TEST DE INTEGRACIÓN?
// ============================================================================
// Un test de INTEGRACIÓN prueba que las piezas funcionen JUNTAS:
//
//     Request HTTP -> Controller -> Service -> DAO -> Base de datos
//     Response HTTP <- Controller <- Service <- DAO <- Base de datos
//
// Se levanta la aplicación Spring REAL completa, se
// manda un request y se mira qué responde y qué quedó guardado.
// ============================================================================

// levanta TODA la aplicación
@SpringBootTest

// nos entrega un objeto MockMvc, que es un "cliente HTTP falso".
// Permite hacer GET/POST/DELETE contra nuestros controllers sin abrir
// un puerto de red real: es rápido y no hace falta tener nada corriendo.
@AutoConfigureMockMvc

// le dice a Spring que además de application.properties
// lea el archivo application-test.properties.
// Ahí está configurada una base de datos H2 EN MEMORIA
@ActiveProfiles("test")
class GimnasioIntegracionTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper; // es la herramienta de Jackson para leer JSON.
	// La usamos para sacar el "id" de las respuestas y poder usarlo en los pedidos siguientes.

	@Autowired
	private InscripcionDAO inscripcionDAO;
	@Autowired
	private ClaseProgramadaDAO claseProgramadaDAO;
	@Autowired
	private ClienteDAO clienteDAO;
	@Autowired
	private ProfesorDAO profesorDAO;
	@Autowired
	private ActividadDAO actividadDAO;

	// ------------------------------------------------------------------------
	// LIMPIEZA
	// ------------------------------------------------------------------------
	@BeforeEach
	void limpiarBaseDeDatos() {
		inscripcionDAO.deleteAll();
		claseProgramadaDAO.deleteAll();
		clienteDAO.deleteAll();
		profesorDAO.deleteAll();
		actividadDAO.deleteAll();
	}

	// ------------------------------------------------------------------------
	// TEST 1
	// ------------------------------------------------------------------------
	@Test
	void noSePuedeRecuperarActividadInexistente() throws Exception {

		mockMvc.perform(get("/actividades/99999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.message").value(containsString("actividad")));
	}

	// ------------------------------------------------------------------------
	// TEST 2: crear algo y comprobar que realmente se guardó en la base
	// ------------------------------------------------------------------------
	@Test
	void crearCliente_seGuardaYSePuedeRecuperarPorIdYEnElListado() throws Exception {

		Long idCliente = crearCliente(40111222L, "ana@test.com", "Ana Perez");

		mockMvc.perform(get("/clientes/{id}", idCliente))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nombre").value("Ana Perez"))
				.andExpect(jsonPath("$.email").value("ana@test.com"))
				.andExpect(jsonPath("$.dni").value(40111222))
				// Un cliente recién creado no tiene inscripciones.
				.andExpect(jsonPath("$.misInscripciones", hasSize(0)));


		mockMvc.perform(get("/clientes"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	// ------------------------------------------------------------------------
	// TEST 3: validaciones de los DTO
	// ------------------------------------------------------------------------
	@Test
	void noSePuedeCrearYGuardaNada_conDatosInvalidos() throws Exception {
		// Email con formato inválido: el DTO tiene @Email
		String clienteConEmailInvalido = """
                {
                  "dni": 40111222,
                  "email": "esto-no-es-un-email",
                  "password": "password123",
                  "nombre": "Ana Perez",
                  "telefono": "1100000000"
                }
                """;

		mockMvc.perform(post("/clientes")
						.contentType(MediaType.APPLICATION_JSON)
						.content(clienteConEmailInvalido))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400));

		// Actividad con cupo 0: el DTO exige @Min(1).
		String actividadSinCupo = """
                {
                  "nombre": "Yoga",
                  "descripcion": "Clase de yoga",
                  "cupo": 0
                }
                """;

		mockMvc.perform(post("/actividades")
						.contentType(MediaType.APPLICATION_JSON)
						.content(actividadSinCupo))
				.andExpect(status().isBadRequest());


		mockMvc.perform(get("/clientes"))
				.andExpect(jsonPath("$", hasSize(0)));
		mockMvc.perform(get("/actividades"))
				.andExpect(jsonPath("$", hasSize(0)));
	}

	// ------------------------------------------------------------------------
	// TEST 4: EL FLUJO PRINCIPAL DE NEGOCIO (camino feliz)
	// ------------------------------------------------------------------------
	// Recorre el caso de uso completo: armar una clase y que un cliente se inscriba.
	@Test
	void flujoCompleto_unClienteSeInscribeAUnaClase() throws Exception {
		Long idActividad = crearActividad("Yoga", 20);
		Long idProfesor = crearProfesor(23456789L, "profe@test.com");
		Long idCliente = crearCliente(40111222L, "ana@test.com", "Ana Perez");

		Long idClase = crearClase("LUNES", "10:00:00", "11:00:00", idProfesor, idActividad, "ACTIVA");

		// Verificamos que la clase quedó guardada con los datos correctos.
		mockMvc.perform(get("/clases/{id}", idClase))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.dia").value("LUNES"))
				.andExpect(jsonPath("$.estado").value("ACTIVA"))
				.andExpect(jsonPath("$.idProfesor").value(idProfesor.intValue()))
				.andExpect(jsonPath("$.idActividad").value(idActividad.intValue()));

		// el cliente se suscribe a la clase.
		suscribir(idClase, idCliente).andExpect(status().isNoContent());

		mockMvc.perform(get("/clientes/{id}", idCliente))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.misInscripciones", hasSize(1)))
				.andExpect(jsonPath("$.misInscripciones[0].idClaseProgramada").value(idClase.intValue()))
				.andExpect(jsonPath("$.misInscripciones[0].dniCliente").value(40111222));
	}

	// ------------------------------------------------------------------------
	// TEST 5: regla de negocio "no puede haber dos clases superpuestas" (TODO EN REVISION)
	// ------------------------------------------------------------------------
	@Test
	void crearClase_conHorarioSuperpuesto_devuelve409_peroUnaContiguaSiSePermite() throws Exception {
		Long idActividad = crearActividad("Yoga", 20);
		Long idProfesor = crearProfesor(23456789L, "profe@test.com");

		// Clase existente: lunes de 10:00 a 11:00.
		crearClase("LUNES", "10:00:00", "11:00:00", idProfesor, idActividad, "ACTIVA");

		// Intento crear otra el mismo día de 10:30 a 11:30.
		pedirCrearClase("LUNES", "10:30:00", "11:30:00", idProfesor, idActividad, "ACTIVA")
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409))
				.andExpect(jsonPath("$.message").value(containsString("Ya existe una clase")));

		// Una clase que arranca justo cuando termina la otra (11:00) NO es conflicto.
		pedirCrearClase("LUNES", "11:00:00", "12:00:00", idProfesor, idActividad, "ACTIVA")
				.andExpect(status().isOk());

		// Mismo horario pero otro día tampoco es conflicto.
		pedirCrearClase("MARTES", "10:00:00", "11:00:00", idProfesor, idActividad, "ACTIVA")
				.andExpect(status().isOk());

		// En la base tienen que haber quedado 3 clases.
		mockMvc.perform(get("/clases"))
				.andExpect(jsonPath("$", hasSize(3)));
	}

	// ------------------------------------------------------------------------
	// TEST 6: horario inválido
	// ------------------------------------------------------------------------
	@Test
	void noSePuedeCrearClaseconHoraFinAnteriorALaDeInicio() throws Exception {
		Long idActividad = crearActividad("Yoga", 20);
		Long idProfesor = crearProfesor(23456789L, "profe@test.com");

		pedirCrearClase("LUNES", "11:00:00", "10:00:00", idProfesor, idActividad, "ACTIVA")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("La hora de inicio debe ser anterior a la hora de fin."));

		mockMvc.perform(get("/clases"))
				.andExpect(jsonPath("$", hasSize(0)));
	}

	// ------------------------------------------------------------------------
	// TEST 7: referencias a cosas que no existen
	// ------------------------------------------------------------------------
	@Test
	void noSePuedeCrearClase_conProfesorInexistente() throws Exception {
		Long idActividad = crearActividad("Yoga", 20);

		pedirCrearClase("LUNES", "10:00:00", "11:00:00", 99999L, idActividad, "ACTIVA")
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));
	}

	@Test
	void noSePuedeSuscribir_conClaseOClienteInexistente() throws Exception {
		Long idActividad = crearActividad("Yoga", 20);
		Long idProfesor = crearProfesor(23456789L, "profe@test.com");
		Long idCliente = crearCliente(40111222L, "ana@test.com", "Ana Perez");
		Long idClase = crearClase("LUNES", "10:00:00", "11:00:00", idProfesor, idActividad, "ACTIVA");

		suscribir(99999L, idCliente).andExpect(status().isNotFound());

		suscribir(idClase, 99999L).andExpect(status().isNotFound());
	}

	// ------------------------------------------------------------------------
	// TEST 8: no se puede inscribir dos veces el mismo cliente
	// ------------------------------------------------------------------------
	@Test
	void noSePuedeSuscribir_dosVecesElMismoCliente() throws Exception {
		Long idActividad = crearActividad("Yoga", 20);
		Long idProfesor = crearProfesor(23456789L, "profe@test.com");
		Long idCliente = crearCliente(40111222L, "ana@test.com", "Ana Perez");
		Long idClase = crearClase("LUNES", "10:00:00", "11:00:00", idProfesor, idActividad, "ACTIVA");

		suscribir(idClase, idCliente).andExpect(status().isNoContent());

		// ...la segunda tiene que ser rechazada.
		suscribir(idClase, idCliente)
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409));

		// Y no se tiene que haber creado una segunda inscripción: sigue habiendo UNA.
		mockMvc.perform(get("/clientes/{id}", idCliente))
				.andExpect(jsonPath("$.misInscripciones", hasSize(1)));
	}

	// ------------------------------------------------------------------------
	// TEST 9: cupo agotado
	// ------------------------------------------------------------------------
	@Test
	void noSePuedeSuscribir_cuandoNoQuedaCupo() throws Exception {
		Long idActividad = crearActividad("Funcional", 1);
		Long idProfesor = crearProfesor(23456789L, "profe@test.com");
		Long idPrimerCliente = crearCliente(40111222L, "ana@test.com", "Ana Perez");
		Long idSegundoCliente = crearCliente(40333444L, "beto@test.com", "Beto Gomez");
		Long idClase = crearClase("LUNES", "10:00:00", "11:00:00", idProfesor, idActividad, "ACTIVA");

		suscribir(idClase, idPrimerCliente).andExpect(status().isNoContent());

		suscribir(idClase, idSegundoCliente)
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409));

		mockMvc.perform(get("/clientes/{id}", idSegundoCliente))
				.andExpect(jsonPath("$.misInscripciones", hasSize(0)));
	}

	// ------------------------------------------------------------------------
	// TEST 10: clase que no está ACTIVA
	// ------------------------------------------------------------------------
	@Test
	void noSePuedeSuscribir_aUnaClaseInactiva() throws Exception {
		Long idActividad = crearActividad("Yoga", 20);
		Long idProfesor = crearProfesor(23456789L, "profe@test.com");
		Long idCliente = crearCliente(40111222L, "ana@test.com", "Ana Perez");

		Long idClase = crearClase("LUNES", "10:00:00", "11:00:00", idProfesor, idActividad, "INACTIVA"); //TODO CAMBAIR EL ESTADO CUANDO SE REFACTORICE

		suscribir(idClase, idCliente)
				.andExpect(status().isConflict());
	}

	// ------------------------------------------------------------------------
	// TEST 11: no se puede borrar una actividad que está en uso
	// ------------------------------------------------------------------------
	@Test
	void noSePuedeEliminarActividadEnUso_yDespuesDeBorrarLaClaseSiSePuede() throws Exception {
		Long idActividad = crearActividad("Yoga", 20);
		Long idProfesor = crearProfesor(23456789L, "profe@test.com");
		Long idClase = crearClase("LUNES", "10:00:00", "11:00:00", idProfesor, idActividad, "ACTIVA");

		mockMvc.perform(delete("/actividades/{id}", idActividad))
				.andExpect(status().isConflict());

		mockMvc.perform(delete("/clases/{id}", idClase))
				.andExpect(status().isOk());

		// ...ahora la actividad ya no está en uso y se puede borrar.
		mockMvc.perform(delete("/actividades/{id}", idActividad))
				.andExpect(status().isOk());

		// Y confirmamos que realmente desapareció de la base.
		mockMvc.perform(get("/actividades/{id}", idActividad))
				.andExpect(status().isNotFound());
	}

	// ========================================================================
	// MÉTODOS AUXILIARES (helpers)
	// ========================================================================
	// No son tests (no tienen @Test). Sirven para no repetir código.

	private Long crearActividad(String nombre, int cupo) throws Exception {
		String json = """
                {
                  "nombre": "%s",
                  "descripcion": "Descripcion de prueba",
                  "cupo": %d
                }
                """.formatted(nombre, cupo);

		return crearYObtenerId("/actividades", json);
	}

	private Long crearProfesor(Long dni, String email) throws Exception {
		// Ojo: Usuario valida que password tenga 8+ caracteres y telefono 10+.
		String json = """
                {
                  "dni": %d,
                  "email": "%s",
                  "password": "password123",
                  "nombre": "Profesor Test",
                  "telefono": "1144556677",
                  "biografia": "Biografia de prueba",
                  "especialidades": "Yoga"
                }
                """.formatted(dni, email);

		return crearYObtenerId("/profesores", json);
	}

	private Long crearCliente(Long dni, String email, String nombre) throws Exception {
		String json = """
                {
                  "dni": %d,
                  "email": "%s",
                  "password": "password123",
                  "nombre": "%s",
                  "telefono": "1100000000"
                }
                """.formatted(dni, email, nombre);

		return crearYObtenerId("/clientes", json);
	}


	private Long crearClase(String dia, String horaInicio, String horaFin, Long idProfesor, Long idActividad, String estado) throws Exception {
		String respuesta = pedirCrearClase(dia, horaInicio, horaFin, idProfesor, idActividad, estado)
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		return leerId(respuesta);
	}

	private ResultActions pedirCrearClase(String dia, String horaInicio, String horaFin, Long idProfesor, Long idActividad, String estado) throws Exception {
		String json = """
                {
                  "dia": "%s",
                  "horaInicio": "%s",
                  "horaFin": "%s",
                  "idProfesor": %d,
                  "idActividad": %d,
                  "estado": "%s"
                }
                """.formatted(dia, horaInicio, horaFin, idProfesor, idActividad, estado);

		return mockMvc.perform(post("/clases")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json));
	}


	private ResultActions suscribir(Long idClase, Long idCliente) throws Exception {
		String json = """
                {
                  "idCliente": %d
                }
                """.formatted(idCliente);

		return mockMvc.perform(post("/clases/{id}/suscripcion", idClase)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json));
	}

	private Long crearYObtenerId(String url, String json) throws Exception {
		String respuesta = mockMvc.perform(post(url)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		return leerId(respuesta);
	}

	private Long leerId(String jsonRespuesta) throws Exception {
		return objectMapper.readTree(jsonRespuesta).get("id").asLong();
	}
}