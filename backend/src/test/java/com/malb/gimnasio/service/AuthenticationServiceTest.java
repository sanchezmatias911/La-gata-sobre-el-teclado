package com.malb.gimnasio.service;

import com.malb.gimnasio.controller.DTO.request.RegistroClienteDTO;
import com.malb.gimnasio.dao.ClienteDAO;
import com.malb.gimnasio.modelo.exception.RecursoDuplicadoException;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private ClienteDAO clienteDAO;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private RegistroClienteDTO dtoValido;

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
}
