
package com.malb.gimnasio;

import com.malb.gimnasio.dao.UsuarioDAO;
import com.malb.gimnasio.modelo.usuario.Administrador;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AdministradorInicialTest {

    private UsuarioDAO usuarioDAO;
    private PasswordEncoder passwordEncoder;
    private AdministradorInicial administradorInicial;

    @BeforeEach
    void setUp() {
        usuarioDAO = mock(UsuarioDAO.class);
        passwordEncoder = mock(PasswordEncoder.class);

        administradorInicial = new AdministradorInicial(
                usuarioDAO,
                passwordEncoder
        );

        configurar(
                "admin@test.com",
                "Admin123456",
                20111222L
        );
    }

    private void configurar(
            String email,
            String password,
            Long dni
    ) {
        ReflectionTestUtils.setField(
                administradorInicial, "email", email
        );

        ReflectionTestUtils.setField(
                administradorInicial, "password", password
        );

        ReflectionTestUtils.setField(
                administradorInicial, "dni", dni
        );

        ReflectionTestUtils.setField(
                administradorInicial, "nombre", "Administrador"
        );

        ReflectionTestUtils.setField(
                administradorInicial, "telefono", "1100000000"
        );
    }

    @Test
    void run_noCreaAdministradorCuandoEmailEsNulo() {

        configurar(null, "Admin123456", 20111222L);

        assertDoesNotThrow(() ->
                administradorInicial.run(mock(ApplicationArguments.class))
        );

        verifyNoInteractions(usuarioDAO);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void run_noCreaAdministradorCuandoEmailEstaVacio() {

        configurar("   ", "Admin123456", 20111222L);

        administradorInicial.run(mock(ApplicationArguments.class));

        verifyNoInteractions(usuarioDAO);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void run_noCreaAdministradorCuandoPasswordEstaVacio() {

        configurar("admin@test.com", " ", 20111222L);

        administradorInicial.run(mock(ApplicationArguments.class));

        verifyNoInteractions(usuarioDAO);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void run_noCreaAdministradorCuandoDniEsNulo() {

        configurar("admin@test.com", "Admin123456", null);

        administradorInicial.run(mock(ApplicationArguments.class));

        verifyNoInteractions(usuarioDAO);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void run_noCreaAdministradorSiYaExiste() {

        when(usuarioDAO.existsByEmailIgnoreCase("admin@test.com"))
                .thenReturn(true);

        administradorInicial.run(mock(ApplicationArguments.class));

        verify(usuarioDAO).existsByEmailIgnoreCase("admin@test.com");
        verify(usuarioDAO, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void run_lanzaExcepcionCuandoPasswordEsMuyCorta() {

        configurar("admin@test.com", "1234567", 20111222L);

        when(usuarioDAO.existsByEmailIgnoreCase("admin@test.com"))
                .thenReturn(false);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> administradorInicial.run(
                        mock(ApplicationArguments.class)
                )
        );

        assertTrue(exception.getMessage().contains("8 caracteres"));

        verify(usuarioDAO, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void run_creaAdministradorCorrectamente() {

        configurar(
                " ADMIN@Test.COM ",
                "Admin123456",
                20111222L
        );

        when(usuarioDAO.existsByEmailIgnoreCase("ADMIN@Test.COM"))
                .thenReturn(false);

        when(passwordEncoder.encode("Admin123456"))
                .thenReturn("password-encriptada");

        administradorInicial.run(mock(ApplicationArguments.class));

        verify(passwordEncoder).encode("Admin123456");

        verify(usuarioDAO).save(argThat(usuario ->
                usuario instanceof Administrador
                        && usuario.getEmail().equals("admin@test.com")
                        && usuario.getDni().equals(20111222L)
                        && usuario.getNombre().equals("Administrador")
                        && ((Administrador) usuario).getPassword()
                        .equals("password-encriptada")
        ));
    }
}
