package com.malb.gimnasio.controller.DTO.request;

import com.malb.gimnasio.modelo.Actividad;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearActividadDTO(
    @NotNull @NotBlank
    String nombre,

    @NotNull
    @NotBlank
    String descripcion,

    @Min(1) @Max(50)
    int cupo
) {
        public Actividad aModelo() {
            return new Actividad(
                    this.nombre,
                    this.descripcion,
                    this.cupo
            );
        }
}
