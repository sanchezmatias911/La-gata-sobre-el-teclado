package com.malb.gimnasio.service;

import com.malb.gimnasio.modelo.usuario.Cliente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ClienteServiceTest {

    @Autowired
    private ClienteService clienteService;

    @Test
    void crear() {

        Cliente cliente = new Cliente(50111222L, "cliente@test.com", "password", "Cliente Test", "1100000000");

        clienteService.crear(cliente);

        assertNotNull(cliente.getDni());
    }

    @Test
    void recuperar() {

        Cliente cliente = new Cliente(50222333L, "cliente2@test.com", "password", "Cliente Test 2", "1100000001");

        clienteService.crear(cliente);

        Cliente recuperado = clienteService.recuperar(cliente.getId());

        assertNotNull(recuperado);
        assertEquals(cliente.getDni(), recuperado.getDni());
        assertEquals("Cliente Test 2", recuperado.getNombre());
    }

    @Test
    void recuperarTodos() {

        List<Cliente> clientes = clienteService.recuperarTodos();

        assertNotNull(clientes);
    }

    @Test
    void eliminar() {

        Cliente cliente = new Cliente(50333444L, "cliente3@test.com", "password", "Cliente Test 3", "1100000002");

        clienteService.crear(cliente);

        Long id = cliente.getId();

        clienteService.eliminar(id);

        assertThrows(RuntimeException.class, () -> clienteService.recuperar(id));
    }

    @AfterEach
    public void tearDown() {
        clienteService.eliminarTodos();
    }
}