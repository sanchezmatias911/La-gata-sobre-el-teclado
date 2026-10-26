package com.malb.gimnasio.controller.DTO.response;

import com.malb.gimnasio.modelo.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record ClaseProgramadaDTO(
        Long id,
        Dia dia,
        LocalTime horaInicio,
        LocalTime horaFin,
        EstadoClaseProgramada estado,
        Long idProfesor,
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
                clase.getProfesor().getId(),
                clase.getActividad().getId(),
                clase.getActividad().getNombre(),
                clase.cantidadInscriptos()
        );
    }

}
