package com.malb.gimnasio.controller.DTO;

import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.usuario.Profesor;

import java.util.List;

public record ProfesorDTO (Long dni, String email, String password, String nombre, String telefono,
                           String biografia, String especialidades, List<ClaseProgramada> misClases) {
    public static ProfesorDTO desdeModelo(Profesor profesor) {
        return new ProfesorDTO(
                profesor.getDni(),
                profesor.getEmail(),
                profesor.getPassword(),
                profesor.getNombre(),
                profesor.getTelefono(),
                profesor.getBiografia(),
                profesor.getEspecialidades(),
                profesor.getMisClases());
    }

    public Profesor aModelo() {
        Profesor profesor = new Profesor(this.dni(), this.email(), this.password(), this.nombre(),
                this.telefono(), this.biografia(), this.especialidades());
        profesor.setMisClases(this.misClases);
        return profesor;
    }
}
