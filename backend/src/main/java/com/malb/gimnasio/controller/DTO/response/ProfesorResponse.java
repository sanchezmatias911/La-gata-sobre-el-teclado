package com.malb.gimnasio.controller.DTO.response;

import com.malb.gimnasio.modelo.usuario.Profesor;

public record ProfesorResponse(
        Long id,
        String nombre
) {
    public static ProfesorResponse desdeModelo(Profesor profesor) {
        return new ProfesorResponse(
                profesor.getId(),
                profesor.getNombre()
        );
    }

}
