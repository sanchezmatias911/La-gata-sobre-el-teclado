package com.malb.gimnasio.controller.DTO;

import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.usuario.Cliente;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record InscripcionDTO(
        @NotNull
        Long id,
        @NotNull
        LocalDateTime fechaDeInscripcion, //tiene sentido? los parametros de aca se usan para llevarlo a modelo, pero ya se hace un LocalDateTime.now()
        @NotNull
        Cliente cliente,
        @NotNull
        ClaseProgramada claseProgramada
) {
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
