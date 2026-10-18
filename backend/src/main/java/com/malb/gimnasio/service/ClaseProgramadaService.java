package com.malb.gimnasio.service;

import com.malb.gimnasio.modelo.ClaseProgramada;

import java.util.List;

public interface ClaseProgramadaService {
    void crear(ClaseProgramada claseProgramada);
    ClaseProgramada recuperar(Long id);
    List<ClaseProgramada> recuperarTodos();
    void eliminar(Long id);
    void eliminarTodos();
}
