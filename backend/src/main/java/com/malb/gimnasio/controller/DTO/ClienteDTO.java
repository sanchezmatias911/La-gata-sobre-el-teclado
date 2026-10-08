package com.malb.gimnasio.controller.DTO;

import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.usuario.Cliente;

import java.util.List;

public record ClienteDTO(Long dni, String email, String password, String nombre, String telefono, List<Inscripcion> misInscripciones) {
    public static ClienteDTO desdeModelo(Cliente cliente) {
        return new ClienteDTO(
                cliente.getDni(),
                cliente.getEmail(),
                cliente.getPassword(),
                cliente.getNombre(),
                cliente.getTelefono(),
                cliente.getMisInscripciones());
    }

    public Cliente aModelo() {
        Cliente cliente = new Cliente(this.dni(), this.email(), this.password(), this.nombre(), this.telefono());
        cliente.setMisInscripciones(this.misInscripciones);
        return cliente;
    }
}
