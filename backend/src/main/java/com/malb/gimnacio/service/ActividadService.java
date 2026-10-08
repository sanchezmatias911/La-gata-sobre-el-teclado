package com.malb.gimnacio.service;

import com.malb.gimnacio.modelo.Actividad;

import java.util.List;

public interface ActividadService {
    void crear(Actividad actividad);
    Actividad recuperar(Long id);
    List<Actividad> recuperarTodos();
    void eliminar(Long id);
}
