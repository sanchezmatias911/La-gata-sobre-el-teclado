package com.malb.gimnacio.controller.DTO;

import com.malb.gimnacio.modelo.Actividad;

public record ActividadDTO(Long id, String nombre, String descripcion, int cupo) {
    public static ActividadDTO desdeModelo(Actividad actividad){
        return new ActividadDTO(
                actividad.getId(),
                actividad.getNombre(),
                actividad.getDescripcion(),
                actividad.getCupo());
    }

    public Actividad aModelo(){
        return new Actividad(this.nombre, this.descripcion, this.cupo);
    }
}
