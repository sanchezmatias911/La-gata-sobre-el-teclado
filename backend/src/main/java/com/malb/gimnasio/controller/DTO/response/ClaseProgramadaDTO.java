package com.malb.gimnasio.controller.DTO.response;

import com.malb.gimnasio.modelo.*;

import java.time.LocalTime;

public record ClaseProgramadaDTO(
        Long id,
        Dia dia,
        LocalTime horaInicio,
        LocalTime horaFin,
        EstadoClaseProgramada estado,
        ProfesorResponse profesorResponse,
        Long idActividad,
        String nombreActividad,
        int cantidadInscriptos


) {
    public static ClaseProgramadaDTO desdeModelo(ClaseProgramada clase){
        return new ClaseProgramadaDTO(
                clase.getId(),
                clase.getDia(),
                clase.getHoraInicio(),
                clase.getHoraFin(),
                clase.getEstado(),
                ProfesorResponse.desdeModelo(clase.getProfesor()),
                clase.getActividad().getId(),
                clase.getActividad().getNombre(),
                clase.cantidadInscriptos()
        );
    }

}
