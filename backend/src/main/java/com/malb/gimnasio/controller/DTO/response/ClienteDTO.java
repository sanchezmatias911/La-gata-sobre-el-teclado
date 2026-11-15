package com.malb.gimnasio.controller.DTO.response;

import com.malb.gimnasio.modelo.usuario.Cliente;

import java.util.List;

public record ClienteDTO(
        Long id,
        Long dni,
        String email,
        String nombre,
        String telefono,
        List<InscripcionDTO> misInscripciones
) {
    public static ClienteDTO desdeModelo(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getDni(),
                cliente.getEmail(),
                cliente.getNombre(),
                cliente.getTelefono(),
                cliente.getMisInscripciones().stream()
                        .map(InscripcionDTO::desdeModelo)
                        .toList()
        );
    }
}
