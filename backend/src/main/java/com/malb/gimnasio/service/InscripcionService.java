package com.malb.gimnasio.service;

import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.usuario.Cliente;

import java.util.List;

public interface InscripcionService {
    Inscripcion crear(Long cliente, Long clase);
    Inscripcion recuperar(Long id);
    List<Inscripcion> recuperarTodos();
    void eliminar(Long id);
    void eliminarTodos();
}
