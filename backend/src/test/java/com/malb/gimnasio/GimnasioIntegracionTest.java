
package com.malb.gimnasio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.malb.gimnasio.dao.ActividadDAO;
import com.malb.gimnasio.dao.ClaseProgramadaDAO;
import com.malb.gimnasio.dao.ClienteDAO;
import com.malb.gimnasio.dao.InscripcionDAO;
import com.malb.gimnasio.dao.ProfesorDAO;
import com.malb.gimnasio.service.LimpiezaBaseDeDatosService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import com.malb.gimnasio.security.WithUsuarioAutenticado;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// ============================================================================
// TEST DE INTEGRACIÓN
// Request HTTP -> Controller -> Service -> DAO -> Base de datos
// ============================================================================

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithUsuarioAutenticado
class GimnasioIntegracionTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

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

	@Autowired
	private LimpiezaBaseDeDatosService limpiezaBaseDeDatosService;

	// ------------------------------------------------------------------------
	// LIMPIEZA
	// ------------------------------------------------------------------------

	@BeforeEach
	void limpiarBaseDeDatos() {
		limpiezaBaseDeDatosService.limpiarBaseDeDatos();
	}

	@AfterEach
	void limpiarBaseDeDatosDespues() {
		limpiezaBaseDeDatosService.limpiarBaseDeDatos();
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
	// TEST 2
	// ------------------------------------------------------------------------

	@Test
	void crearCliente_seGuardaYSePuedeRecuperarPorIdYEnElListado() throws Exception {

		Long idCliente = crearCliente(40111222L, "ana@test.com", "Ana Perez");

		mockMvc.perform(get("/clientes/{id}", idCliente))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nombre").value("Ana Perez"))
				.andExpect(jsonPath("$.email").value("ana@test.com"))
				.andExpect(jsonPath("$.dni").value(40111222))
				.andExpect(jsonPath("$.misInscripciones", hasSize(0)));

		mockMvc.perform(get("/clientes"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	// ------------------------------------------------------------------------
	// TEST 3: VALIDACIONES DE LOS DTO
	// ------------------------------------------------------------------------

	@Test
	void noSePuedeCrearYGuardaNada_conDatosInvalidos() throws Exception {

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
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(clienteConEmailInvalido))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400));

		String actividadSinCupo = """
                {
                  "nombre": "Yoga",
                  "descripcion": "Clase de yoga",
                  "cupo": 0
                }
                """;

		mockMvc.perform(post("/actividades")
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(actividadSinCupo))
				.andExpect(status().isBadRequest());

		mockMvc.perform(get("/clientes"))
				.andExpect(jsonPath("$", hasSize(0)));

		mockMvc.perform(get("/actividades"))
				.andExpect(jsonPath("$", hasSize(0)));
	}

	// ------------------------------------------------------------------------
	// TEST 4: FLUJO PRINCIPAL DE NEGOCIO
	// ------------------------------------------------------------------------

	@Test
	void flujoCompleto_unClienteSeInscribeAUnaClase() throws Exception {

		Long idActividad = crearActividad("Yoga", 20);
		Long idProfesor = crearProfesor(23456789L, "profe@test.com");
		Long idCliente = crearCliente(40111222L, "ana@test.com", "Ana Perez");

		Long idClase = crearClase(
				"LUNES", "10:00:00", "11:00:00",
				idProfesor, idActividad, "ACTIVA"
		);

		mockMvc.perform(get("/clases/{id}", idClase))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.dia").value("LUNES"))
				.andExpect(jsonPath("$.estado").value("ACTIVA"))
				.andExpect(jsonPath("$.profesorResponse.id").value(idProfesor.intValue()))
				.andExpect(jsonPath("$.idActividad").value(idActividad.intValue()));

		suscribir(idClase, idCliente)
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/clientes/{id}", idCliente))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.misInscripciones", hasSize(1)))
				.andExpect(jsonPath("$.misInscripciones[0].idClaseProgramada")
						.value(idClase.intValue()))
				.andExpect(jsonPath("$.misInscripciones[0].dniCliente")
						.value(40111222));
	}

	// ------------------------------------------------------------------------
	// TEST 5: HORARIOS SUPERPUESTOS
	// ------------------------------------------------------------------------

	@Test
	void crearClase_conHorarioSuperpuesto_devuelve409_peroUnaContiguaSiSePermite()
			throws Exception {

		Long idActividad = crearActividad("Yoga", 20);
		Long idProfesor = crearProfesor(23456789L, "profe@test.com");

		crearClase("LUNES", "10:00:00", "11:00:00",
				idProfesor, idActividad, "ACTIVA");

		pedirCrearClase("LUNES", "10:30:00", "11:30:00",
				idProfesor, idActividad, "ACTIVA")
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409))
				.andExpect(jsonPath("$.message")
						.value(containsString("Ya existe una clase")));

		pedirCrearClase("LUNES", "11:00:00", "12:00:00",
				idProfesor, idActividad, "ACTIVA")
				.andExpect(status().isOk());

		pedirCrearClase("MARTES", "10:00:00", "11:00:00",
				idProfesor, idActividad, "ACTIVA")
				.andExpect(status().isOk());

		mockMvc.perform(get("/clases"))
				.andExpect(jsonPath("$", hasSize(3)));
	}

	// ------------------------------------------------------------------------
	// TEST 6: HORARIO INVÁLIDO
	// ------------------------------------------------------------------------

	@Test
	void noSePuedeCrearClaseconHoraFinAnteriorALaDeInicio() throws Exception {

		Long idActividad = crearActividad("Yoga", 20);
		Long idProfesor = crearProfesor(23456789L, "profe@test.com");

		pedirCrearClase("LUNES", "11:00:00", "10:00:00",
				idProfesor, idActividad, "ACTIVA")
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message")
						.value("La hora de inicio debe ser anterior a la hora de fin."));

		mockMvc.perform(get("/clases"))
				.andExpect(jsonPath("$", hasSize(0)));
	}

	// ------------------------------------------------------------------------
	// TEST 7: REFERENCIAS INEXISTENTES
	// ------------------------------------------------------------------------

	@Test
	void noSePuedeCrearClase_conProfesorInexistente() throws Exception {

		Long idActividad = crearActividad("Yoga", 20);

		pedirCrearClase("LUNES", "10:00:00", "11:00:00",
				99999L, idActividad, "ACTIVA")
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));
	}

	@Test
	void noSePuedeSuscribir_conClaseOClienteInexistente() throws Exception {

		Long idActividad = crearActividad("Yoga", 20);
		Long idProfesor = crearProfesor(23456789L, "profe@test.com");
		Long idCliente = crearCliente(40111222L, "ana@test.com", "Ana Perez");

		Long idClase = crearClase("LUNES", "10:00:00", "11:00:00",
				idProfesor, idActividad, "ACTIVA");

		suscribir(99999L, idCliente)
				.andExpect(status().isNotFound());

		suscribir(idClase, 99999L)
				.andExpect(status().isNotFound());
	}

	// ------------------------------------------------------------------------
	// TEST 8: INSCRIPCIÓN DUPLICADA
	// ------------------------------------------------------------------------

	@Test
	void noSePuedeSuscribir_dosVecesElMismoCliente() throws Exception {

		Long idActividad = crearActividad("Yoga", 20);
		Long idProfesor = crearProfesor(23456789L, "profe@test.com");
		Long idCliente = crearCliente(40111222L, "ana@test.com", "Ana Perez");

		Long idClase = crearClase("LUNES", "10:00:00", "11:00:00",
				idProfesor, idActividad, "ACTIVA");

		suscribir(idClase, idCliente)
				.andExpect(status().isNoContent());

		suscribir(idClase, idCliente)
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409));

		mockMvc.perform(get("/clientes/{id}", idCliente))
				.andExpect(jsonPath("$.misInscripciones", hasSize(1)));
	}

	// ------------------------------------------------------------------------
	// TEST 9: CUPO AGOTADO
	// ------------------------------------------------------------------------

	@Test
	void noSePuedeSuscribir_cuandoNoQuedaCupo() throws Exception {

		Long idActividad = crearActividad("Funcional", 1);
		Long idProfesor = crearProfesor(23456789L, "profe@test.com");
		Long idPrimerCliente = crearCliente(
				40111222L, "ana@test.com", "Ana Perez");
		Long idSegundoCliente = crearCliente(
				40333444L, "beto@test.com", "Beto Gomez");

		Long idClase = crearClase("LUNES", "10:00:00", "11:00:00",
				idProfesor, idActividad, "ACTIVA");

		suscribir(idClase, idPrimerCliente)
				.andExpect(status().isNoContent());

		suscribir(idClase, idSegundoCliente)
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.status").value(409));

		mockMvc.perform(get("/clientes/{id}", idSegundoCliente))
				.andExpect(jsonPath("$.misInscripciones", hasSize(0)));
	}

	// ------------------------------------------------------------------------
	// TEST 10: CLASE INACTIVA
	// ------------------------------------------------------------------------

	@Test
	void noSePuedeSuscribir_aUnaClaseInactiva() throws Exception {

		Long idActividad = crearActividad("Yoga", 20);
		Long idProfesor = crearProfesor(23456789L, "profe@test.com");
		Long idCliente = crearCliente(40111222L, "ana@test.com", "Ana Perez");

		Long idClase = crearClase("LUNES", "10:00:00", "11:00:00",
				idProfesor, idActividad, "INACTIVA");

		suscribir(idClase, idCliente)
				.andExpect(status().isConflict());
	}

	// ------------------------------------------------------------------------
	// TEST 11: ELIMINAR ACTIVIDAD EN USO
	// ------------------------------------------------------------------------

	@Test
	void noSePuedeEliminarActividadEnUso_yDespuesDeBorrarLaClaseSiSePuede()
			throws Exception {

		Long idActividad = crearActividad("Yoga", 20);
		Long idProfesor = crearProfesor(23456789L, "profe@test.com");

		Long idClase = crearClase("LUNES", "10:00:00", "11:00:00",
				idProfesor, idActividad, "ACTIVA");

		mockMvc.perform(delete("/actividades/{id}", idActividad)
						.with(csrf()))
				.andExpect(status().isConflict());

		mockMvc.perform(delete("/clases/{id}", idClase)
						.with(csrf()))
				.andExpect(status().isOk());

		mockMvc.perform(delete("/actividades/{id}", idActividad)
						.with(csrf()))
				.andExpect(status().isOk());

		mockMvc.perform(get("/actividades/{id}", idActividad))
				.andExpect(status().isNotFound());
	}

	// ========================================================================
	// MÉTODOS AUXILIARES
	// ========================================================================

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

	private Long crearCliente(Long dni, String email, String nombre)
			throws Exception {

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

	private Long crearClase(
			String dia,
			String horaInicio,
			String horaFin,
			Long idProfesor,
			Long idActividad,
			String estado) throws Exception {

		String respuesta = pedirCrearClase(
				dia, horaInicio, horaFin,
				idProfesor, idActividad, estado)
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		return leerId(respuesta);
	}

	private ResultActions pedirCrearClase(
			String dia,
			String horaInicio,
			String horaFin,
			Long idProfesor,
			Long idActividad,
			String estado) throws Exception {

		String json = """
                {
                  "dia": "%s",
                  "horaInicio": "%s",
                  "horaFin": "%s",
                  "idProfesor": %d,
                  "idActividad": %d,
                  "estado": "%s"
                }
                """.formatted(
				dia, horaInicio, horaFin,
				idProfesor, idActividad, estado);

		return mockMvc.perform(post("/clases")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(json));
	}

	private ResultActions suscribir(Long idClase, Long idCliente)
			throws Exception {

		String json = """
                {
                  "idCliente": %d
                }
                """.formatted(idCliente);

		return mockMvc.perform(post("/clases/{id}/suscripcion", idClase)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(json));
	}

	private Long crearYObtenerId(String url, String json)
			throws Exception {

		String respuesta = mockMvc.perform(post(url)
						.with(csrf())
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

		return leerId(respuesta);
	}

	private Long leerId(String jsonRespuesta) throws Exception {
		return objectMapper.readTree(jsonRespuesta)
				.get("id")
				.asLong();
	}
}
