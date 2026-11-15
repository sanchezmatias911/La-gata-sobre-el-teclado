package com.malb.gimnasio.controller.DTO.response;

import com.malb.gimnasio.modelo.Inscripcion;

import java.time.LocalDateTime;

public record InscripcionDTO(
        Long id,
        LocalDateTime fechaDeInscripcion,
        Long dniCliente,
        Long idClaseProgramada
) {
    public static InscripcionDTO desdeModelo(Inscripcion inscripcion){
        return new InscripcionDTO(
                inscripcion.getId(),
                inscripcion.getFechaDeInscripcion(),
                inscripcion.getCliente().getDni(),
                inscripcion.getClaseProgramada().getId()
        );
    }
}
