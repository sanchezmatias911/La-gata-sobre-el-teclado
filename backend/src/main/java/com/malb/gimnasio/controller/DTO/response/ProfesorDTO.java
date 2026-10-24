package com.malb.gimnasio.controller.DTO.response;

import com.malb.gimnasio.modelo.usuario.Profesor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProfesorDTO (
        Long id,
        Long dni,
        String email,
        String password,
        String nombre,
        String telefono,
        String biografia,
        String especialidades
) {
    public static ProfesorDTO desdeModelo(Profesor profesor) {
        return new ProfesorDTO(
                profesor.getId(),
                profesor.getDni(),
                profesor.getEmail(),
                profesor.getPassword(),
                profesor.getNombre(),
                profesor.getTelefono(),
                profesor.getBiografia(),
                profesor.getEspecialidades()
        );
    }

}
