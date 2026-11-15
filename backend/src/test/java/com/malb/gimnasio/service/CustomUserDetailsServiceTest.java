
package com.malb.gimnasio.service;

import com.malb.gimnasio.dao.UsuarioDAO;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.modelo.usuario.Profesor;
import com.malb.gimnasio.security.CustomUserDetails;
import com.malb.gimnasio.security.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioDAO usuarioDAO;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    void buscarClienteDevuelveCustomUserDetails() {
        Cliente cliente = new Cliente(
                40111222L,
                "ana@gmail.com",
                "$2a$12$hashDePrueba",
                "Ana Perez",
                "1156789012"
        );

        when(usuarioDAO.findByEmailIgnoreCase("ana@gmail.com"))
                .thenReturn(Optional.of(cliente));

        UserDetails resultado =
                userDetailsService.loadUserByUsername("ana@gmail.com");

        assertInstanceOf(CustomUserDetails.class, resultado);
        assertEquals("ana@gmail.com", resultado.getUsername());
    }

    @Test
    void buscarProfesorRechazaLaAutenticacion() {
        Profesor profesor = new Profesor(
                30111222L,
                "profesor@gmail.com",
                "Juan Perez",
                "1156789012",
                "Profesor de musculación",
                "Musculación"
        );

        when(usuarioDAO.findByEmailIgnoreCase("profesor@gmail.com"))
                .thenReturn(Optional.of(profesor));

        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("profesor@gmail.com")
        );
    }

    @Test
    void buscarUsuarioInexistenteLanzaExcepcion() {
        when(usuarioDAO.findByEmailIgnoreCase("inexistente@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("inexistente@gmail.com")
        );
    }
}
