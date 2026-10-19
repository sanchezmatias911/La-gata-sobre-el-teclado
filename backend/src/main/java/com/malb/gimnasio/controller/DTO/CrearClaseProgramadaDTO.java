package com.malb.gimnasio.controller.DTO;

import com.malb.gimnasio.modelo.Actividad;
import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Dia;
import com.malb.gimnasio.modelo.EstadoClaseProgramada;
import com.malb.gimnasio.modelo.usuario.Profesor;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record CrearClaseProgramadaDTO(

        @NotNull(message = "El día es obligatorio")
        Dia dia,

        @NotNull(message = "La hora de inicio es obligatoria")
        LocalTime horaInicio,

        @NotNull(message = "La hora de fin es obligatoria")
        LocalTime horaFin,

        @NotNull(message = "El ID del profesor es obligatorio")
        Long idProfesor,

        @NotNull(message = "El ID de la actividad es obligatorio")
        Long idActividad,

        @NotNull
        EstadoClaseProgramada estado


) {
    public ClaseProgramada aModelo(Profesor profesor, Actividad actividad){
        
        return new ClaseProgramada(this.dia, this.horaInicio, this.horaFin, profesor, actividad, this.estado);
    }
}
