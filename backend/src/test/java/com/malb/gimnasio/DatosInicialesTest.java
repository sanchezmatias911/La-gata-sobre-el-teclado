
package com.malb.gimnasio;

import com.malb.gimnasio.dao.UsuarioDAO;
import com.malb.gimnasio.modelo.Actividad;
import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.usuario.Administrador;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.modelo.usuario.Profesor;
import com.malb.gimnasio.service.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatosInicialesTest {

    private ActividadService actividadService;
    private ProfesorService profesorService;
    private ClienteService clienteService;
    private ClaseProgramadaService claseProgramadaService;
    private InscripcionService inscripcionService;
    private LimpiezaBaseDeDatosService limpiezaService;
    private UsuarioDAO usuarioDAO;
    private PasswordEncoder passwordEncoder;

    private DatosIniciales datosIniciales;

    @BeforeEach
    void setUp() {
        actividadService = mock(ActividadService.class);
        profesorService = mock(ProfesorService.class);
        clienteService = mock(ClienteService.class);
        claseProgramadaService = mock(ClaseProgramadaService.class);
        inscripcionService = mock(InscripcionService.class);
        limpiezaService = mock(LimpiezaBaseDeDatosService.class);
        usuarioDAO = mock(UsuarioDAO.class);
        passwordEncoder = mock(PasswordEncoder.class);

        datosIniciales = new DatosIniciales(
                actividadService,
                profesorService,
                clienteService,
                claseProgramadaService,
                inscripcionService,
                limpiezaService,
                usuarioDAO,
                passwordEncoder
        );
    }

    @Test
    void run_cargaTodosLosDatos() {

        when(usuarioDAO.existsByEmailIgnoreCase(
                "admin@gimnasio.com"
        )).thenReturn(true);

        datosIniciales.run(mock(ApplicationArguments.class));

        verify(actividadService, times(6)).crear(any(Actividad.class));
        verify(profesorService, times(3)).crear(any(Profesor.class));
        verify(clienteService, times(3)).crear(any(Cliente.class));
        verify(claseProgramadaService, times(3))
                .crear(any(ClaseProgramada.class));
        verify(inscripcionService, times(3))
                .crear(any(Cliente.class), any(ClaseProgramada.class));
    }

    @Test
    void run_creaAdministradorSiNoExiste() {

        when(usuarioDAO.existsByEmailIgnoreCase(
                "admin@gimnasio.com"
        )).thenReturn(false);

        when(passwordEncoder.encode("Admin123456"))
                .thenReturn("password-encriptada");

        datosIniciales.run(mock(ApplicationArguments.class));

        verify(passwordEncoder).encode("Admin123456");

        verify(usuarioDAO).save(argThat(usuario ->
                usuario instanceof Administrador
                        && usuario.getEmail().equals("admin@gimnasio.com")
                        && usuario.getDni().equals(20111222L)
                        && ((Administrador) usuario).getPassword()
                        .equals("password-encriptada")
        ));
    }

    @Test
    void run_noCreaAdministradorSiYaExiste() {

        when(usuarioDAO.existsByEmailIgnoreCase(
                "admin@gimnasio.com"
        )).thenReturn(true);

        datosIniciales.run(mock(ApplicationArguments.class));

        verify(usuarioDAO, never()).save(any());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void eliminarDatosIniciales_limpiaBaseDeDatos() {

        datosIniciales.eliminarDatosIniciales();

        verify(limpiezaService).limpiarBaseDeDatos();
    }

    @Test
    void eliminarDatosIniciales_noPropagaExcepcion() {

        doThrow(new RuntimeException("Error de limpieza"))
                .when(limpiezaService).limpiarBaseDeDatos();

        assertDoesNotThrow(
                () -> datosIniciales.eliminarDatosIniciales()
        );

        verify(limpiezaService).limpiarBaseDeDatos();
    }
}
