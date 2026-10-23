package com.malb.gimnasio.controller.DTO.request;

import com.malb.gimnasio.modelo.usuario.Profesor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearProfesorDTO(

        @NotNull Long dni,

        @NotNull @NotBlank String email,

        @NotNull @NotBlank String password,

        @NotNull @NotBlank String nombre,

        @NotNull @NotBlank String telefono,

        @NotNull @NotBlank String biografia,

        @NotNull @NotBlank String especialidades

) {
    public Profesor aModelo() {

        return new Profesor(this.dni(), this.email(), this.password(), this.nombre(), this.telefono(), this.biografia(), this.especialidades());

    }
}
