package com.malb.gimnasio.controller.DTO;

import com.malb.gimnasio.modelo.*;
import com.malb.gimnasio.modelo.*;
import com.malb.gimnasio.modelo.usuario.Profesor;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.List;

public record ClaseProgramadaDTO(
        @NotNull
        Long id,

        @NotNull
        Dia dia,

        @NotNull
        LocalTime horaInicio,

        @NotNull
        LocalTime horaFin,

        @NotNull
        EstadoClaseProgramada estado,

        @NotNull
        Profesor profesor,

        @NotNull
        Actividad actividad

) {
    public static ClaseProgramadaDTO desdeModelo(ClaseProgramada clase){
        return new ClaseProgramadaDTO(
                clase.getId(),
                clase.getDia(),
                clase.getHoraInicio(),
                clase.getHoraFin(),
                clase.getEstado(),
                clase.getProfesor(),
                clase.getActividad()
        );
    }

    public ClaseProgramada aModelo(){
        ClaseProgramada clase = new ClaseProgramada(this.dia, this.horaInicio, this.horaFin, this.profesor, this.actividad, this.estado);
        return clase;
    }
}
