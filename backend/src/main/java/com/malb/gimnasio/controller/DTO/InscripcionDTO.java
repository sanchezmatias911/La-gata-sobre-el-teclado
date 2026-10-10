package com.malb.gimnasio.controller.DTO;

import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.usuario.Cliente;

import java.time.LocalDateTime;

public record InscripcionDTO(Long id, LocalDateTime fechaDeInscripcion, Cliente cliente, ClaseProgramada claseProgramada) {
    public static InscripcionDTO desdeModelo(Inscripcion inscripcion){
        return new InscripcionDTO(
                inscripcion.getId(),
                inscripcion.getFechaDeInscripcion(),
                inscripcion.getCliente(),
                inscripcion.getClaseProgramada());
    }

    public Inscripcion aModelo(){
        return new Inscripcion(this.cliente, this.claseProgramada);
    }
}
