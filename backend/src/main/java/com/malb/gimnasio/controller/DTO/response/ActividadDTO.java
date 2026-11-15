package com.malb.gimnasio.controller.DTO.response;

import com.malb.gimnasio.modelo.Actividad;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ActividadDTO(
        Long id,
        String nombre,
        String descripcion,
        int cupo
) {
    public static ActividadDTO desdeModelo(Actividad actividad){
        return new ActividadDTO(
                actividad.getId(),
                actividad.getNombre(),
                actividad.getDescripcion(),
                actividad.getCupo());
    }
}
