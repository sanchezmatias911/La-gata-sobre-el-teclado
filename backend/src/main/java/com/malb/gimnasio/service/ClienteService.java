package com.malb.gimnasio.service;

import com.malb.gimnasio.modelo.usuario.Cliente;

import java.util.List;

public interface ClienteService {
    void crear(Cliente cliente);
    Cliente recuperar(Long id);
    Cliente recuperarPorDNI(Long dni);
    List<Cliente> recuperarTodos();
    void eliminar(Long dni);
    void eliminarTodos();
}
