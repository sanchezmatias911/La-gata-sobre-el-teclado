package com.malb.gimnasio.controller.DTO.request;

import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.usuario.Cliente;
import jakarta.validation.constraints.NotNull;

public record CrearInscripcionDTO(
        @NotNull
        Long idCliente,

        @NotNull
        Long idClaseProgramada
){

    public Inscripcion aModelo(Cliente cliente, ClaseProgramada claseProgramada){
        return new Inscripcion(cliente, claseProgramada);
    }
}
