package com.malb.gimnasio.controller.DTO;

import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.usuario.Profesor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProfesorDTO (
        @NotNull
        Long id,

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

        @NotNull @NotBlank
        String biografia,

        @NotNull @NotBlank
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
