package com.malb.gimnasio.controller.DTO;

import com.malb.gimnasio.modelo.*;
import com.malb.gimnasio.modelo.*;
import com.malb.gimnasio.modelo.usuario.Profesor;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ClaseProgramadaDTO(
        @NotNull
        Long id,

        @NotNull(message = "El día es obligatorio")
        Dia dia,

        @NotNull(message = "La hora de inicio es obligatoria")
        LocalTime horaInicio,

        @NotNull(message = "La hora de fin es obligatoria")
        LocalTime horaFin,

        @NotNull
        EstadoClaseProgramada estado,

        @NotNull
        Long idProfesor,

        @NotNull
        Long idActividad


) {
    public static ClaseProgramadaDTO desdeModelo(ClaseProgramada clase){
        return new ClaseProgramadaDTO(
                clase.getId(),
                clase.getDia(),
                clase.getHoraInicio(),
                clase.getHoraFin(),
                clase.getEstado(),
                clase.getProfesor().getId(),
                clase.getActividad().getId()
        );
    }

}
