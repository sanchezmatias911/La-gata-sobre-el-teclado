package com.malb.gimnasio.service;

import com.malb.gimnasio.modelo.Inscripcion;

import java.util.List;

public interface InscripcionService {
    void crear(Inscripcion inscripcion);
    Inscripcion recuperar(Long id);
    List<Inscripcion> recuperarTodos();
    void eliminar(Long id);
    void eliminarTodos();
}
