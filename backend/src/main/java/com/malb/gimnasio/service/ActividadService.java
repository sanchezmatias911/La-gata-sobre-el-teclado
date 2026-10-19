package com.malb.gimnasio.service;

import com.malb.gimnasio.modelo.Actividad;

import java.util.List;

public interface ActividadService {
    void crear(Actividad actividad);
    Actividad recuperar(Long id);
    List<Actividad> recuperarTodos();
    void eliminar(Long id);
    void eliminarTodos();
}
