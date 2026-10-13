package com.malb.gimnasio.controller.DTO;

import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.usuario.Cliente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ClienteDTO(
        @NotNull
        Long dni,

        @NotNull @NotBlank
        String email,

        @NotNull @NotBlank
        String password,

        @NotNull @NotBlank
        String nombre,

        @NotNull @NotBlank
        String telefono,

        @NotNull
        List<Inscripcion> misInscripciones // tiene sentido que se den ya las inscrpciones cuando se crea el cliente?
) {
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
