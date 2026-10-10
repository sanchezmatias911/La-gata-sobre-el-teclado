package com.malb.gimnasio.controller.DTO;

import com.malb.gimnasio.modelo.*;
import com.malb.gimnasio.modelo.*;
import com.malb.gimnasio.modelo.usuario.Profesor;

import java.time.LocalTime;
import java.util.List;

public record ClaseProgramadaDTO(Long id, Dia dia, LocalTime horaInicio, LocalTime horaFin,
                                 EstadoClaseProgramada estado, Profesor profesor, Actividad actividad,
                                 List<Inscripcion> inscripciones) {
    public static ClaseProgramadaDTO desdeModelo(ClaseProgramada clase){
        return new ClaseProgramadaDTO(
                clase.getId(),
                clase.getDia(),
                clase.getHoraInicio(),
                clase.getHoraFin(),
                clase.getEstado(),
                clase.getProfesor(),
                clase.getActividad(),
                clase.getInscripciones());
    }

    public ClaseProgramada aModelo(){
        ClaseProgramada clase = new ClaseProgramada(this.dia, this.horaInicio, this.horaFin, this.profesor,
                this.actividad, this.estado);
        clase.setInscripciones(this.inscripciones);
        return clase;
    }
}
