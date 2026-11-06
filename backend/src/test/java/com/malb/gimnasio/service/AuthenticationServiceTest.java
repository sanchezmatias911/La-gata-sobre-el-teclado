package com.malb.gimnasio.service;

import com.malb.gimnasio.controller.DTO.request.RegistroClienteDTO;
import com.malb.gimnasio.dao.ClienteDAO;
import com.malb.gimnasio.modelo.exception.RecursoDuplicadoException;
import com.malb.gimnasio.modelo.usuario.Administrador;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.security.CustomUserDetailsService;
import com.malb.gimnasio.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.malb.gimnasio.controller.DTO.request.CrearLoginDTO;
import com.malb.gimnasio.controller.DTO.response.LoginDTO;
import com.malb.gimnasio.modelo.usuario.Rol;
import com.malb.gimnasio.security.CustomUserDetails;
import com.malb.gimnasio.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private ClienteDAO clienteDAO;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    @Mock
    private PasswordEncoder passwordEncoder;

    private RegistroClienteDTO dtoValido;
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;


    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp() {
        dtoValido = new RegistroClienteDTO(
                "maRia",
                "pereZ",
                "Maria@Gmail.com",
                "contra123",
                "1123456789",
                34675645L
        );
        lenient().when(passwordEncoder.encode("contra123"))
                .thenReturn("$2a$12$hashDePrueba");
    }

    @Test
    void registrarGuardaClienteCuandoLosDatosSonValidos() {
        when(clienteDAO.existsByEmail("maria@gmail.com")).thenReturn(false);

        authenticationService.registrar(dtoValido);

        verify(clienteDAO).save(any(Cliente.class));
    }

    @Test
    void registrarNormalizaElMail() {
        when(clienteDAO.existsByEmail("maria@gmail.com")).thenReturn(false);

        authenticationService.registrar(dtoValido);

        verify(clienteDAO).existsByEmail("maria@gmail.com");
    }

    @Test
    void registrarCapitalizaNombreYApellido() {
        when(clienteDAO.existsByEmail("maria@gmail.com")).thenReturn(false);

        //captura el objeto que se le pasa para después comprobar los datos
        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);

        authenticationService.registrar(dtoValido);

        verify(clienteDAO).save(captor.capture());

        Cliente clienteGuardado = captor.getValue();

        assertEquals("Maria Perez", clienteGuardado.getNombre());
    }

    @Test
    void registrarNoGuardaSiElMailYaExiste() {
        when(clienteDAO.existsByEmail("maria@gmail.com")).thenReturn(true);

        assertThrows(RecursoDuplicadoException.class, () -> authenticationService.registrar(dtoValido));

        verify(clienteDAO, never()).save(any(Cliente.class));
    }

    @Test
    void registrarGuardaElTelefonoSinEspaciosEnLosExtremos() {
        when(clienteDAO.existsByEmail("maria@gmail.com")).thenReturn(false);

        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);

        authenticationService.registrar(dtoValido);

        verify(clienteDAO).save(captor.capture());

        assertEquals("1123456789", captor.getValue().getTelefono());
    }
    @Test
    void registrarGuardaLaPasswordEncriptada() {
        when(clienteDAO.existsByEmail("maria@gmail.com")).thenReturn(false);

        ArgumentCaptor<Cliente> captor = ArgumentCaptor.forClass(Cliente.class);

        authenticationService.registrar(dtoValido);

        verify(clienteDAO).save(captor.capture());

        assertEquals(
                "$2a$12$hashDePrueba",
                captor.getValue().getPassword()
        );

        verify(passwordEncoder).encode("contra123");
    }

    @Test
    void loginConCredencialesValidasDevuelveTokenYDatosDelUsuario() {
        CrearLoginDTO dto = new CrearLoginDTO(
                "ana@mail.com",
                "password123"
        );

        Cliente cliente = new Cliente(
                40111222L,
                "ana@mail.com",
                "$2a$12$hashDePrueba",
                "Ana Perez",
                "1156789012"
        );

        ReflectionTestUtils.setField(cliente, "id", 1L);

        CustomUserDetails usuario = new CustomUserDetails(cliente);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        usuario.getAuthorities()
                );

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(authentication);

        when(jwtService.generarToken(usuario))
                .thenReturn("token-de-prueba");

        when(jwtService.getExpiracionSegundos())
                .thenReturn(7200L);

        LoginDTO resultado = authenticationService.login(dto);

        assertEquals("token-de-prueba", resultado.token());
        assertEquals("Bearer", resultado.tipo());
        assertEquals(7200L, resultado.vencimiento());

        assertEquals(1L, resultado.usuario().id());
        assertEquals("Ana Perez", resultado.usuario().nombre());
        assertEquals("ana@mail.com", resultado.usuario().email());
        assertEquals(Rol.CLIENTE, resultado.usuario().rol());

        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(jwtService).generarToken(usuario);
    }

    @Test
    void loginNormalizaElEmailAntesDeAutenticar() {
        CrearLoginDTO dto = new CrearLoginDTO(
                " ANA@GMAIL.COM ",
                "password123"
        );

        Cliente cliente = new Cliente(
                40111222L,
                "ana@gmail.com",
                "$2a$12$hashDePrueba",
                "Ana Perez",
                "1156789012"
        );

        ReflectionTestUtils.setField(cliente, "id", 1L);

        CustomUserDetails usuario = new CustomUserDetails(cliente);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        usuario.getAuthorities()
                );

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(authentication);

        when(jwtService.generarToken(usuario))
                .thenReturn("token-de-prueba");

        when(jwtService.getExpiracionSegundos())
                .thenReturn(7200L);

        authenticationService.login(dto);

        ArgumentCaptor<Authentication> captor =
                ArgumentCaptor.forClass(Authentication.class);

        verify(authenticationManager).authenticate(captor.capture());

        assertEquals(
                "ana@gmail.com",
                captor.getValue().getPrincipal()
        );

        assertEquals(
                "password123",
                captor.getValue().getCredentials()
        );
    }

    @Test
    void loginConCredencialesIncorrectasLanzaExcepcion() {
        CrearLoginDTO dto = new CrearLoginDTO(
                "ana@mail.com",
                "contraseñaIncorrecta"
        );

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Credenciales incorrectas"));

        assertThrows(
                BadCredentialsException.class,
                () -> authenticationService.login(dto)
        );

        verify(jwtService, never()).generarToken(any());
    }

    @Test
    void loginConUsuarioInexistenteLanzaExcepcion() {
        CrearLoginDTO dto = new CrearLoginDTO(
                "inexistente@mail.com",
                "password123"
        );

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Credenciales incorrectas"));

        assertThrows(
                BadCredentialsException.class,
                () -> authenticationService.login(dto)
        );

        verify(jwtService, never()).generarToken(any());
    }

    @Test
    void loginConAdministradorDevuelveRolAdmin() {
        CrearLoginDTO dto = new CrearLoginDTO(
                "admin@gmail.com",
                "password123"
        );

        Administrador administrador = new Administrador(
                30111222L,
                "admin@gmail.com",
                "$2a$12$hashDePrueba",
                "Administrador",
                "1156789012"
        );

        ReflectionTestUtils.setField(administrador, "id", 2L);

        CustomUserDetails usuario = new CustomUserDetails(administrador);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        usuario.getAuthorities()
                );

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(authentication);

        when(jwtService.generarToken(usuario))
                .thenReturn("token-admin");

        when(jwtService.getExpiracionSegundos())
                .thenReturn(7200L);

        LoginDTO resultado = authenticationService.login(dto);

        assertEquals("token-admin", resultado.token());
        assertEquals(Rol.ADMIN, resultado.usuario().rol());
    }


}
