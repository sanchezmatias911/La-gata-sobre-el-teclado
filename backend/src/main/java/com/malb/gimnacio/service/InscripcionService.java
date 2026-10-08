package com.malb.gimnacio.service;

import com.malb.gimnacio.modelo.Inscripcion;

import java.util.List;

public interface InscripcionService {
    void crear(Inscripcion inscripcion);
    Inscripcion recuperar(Long id);
    List<Inscripcion> recuperarTodos();
    void eliminar(Long id);
}
