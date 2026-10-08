package com.malb.gimnacio.service;

import com.malb.gimnacio.modelo.ClaseProgramada;

import java.util.List;

public interface ClaseProgramadaService {
    void crear(ClaseProgramada claseProgramada);
    ClaseProgramada recuperar(Long id);
    List<ClaseProgramada> recuperarTodos();
    void eliminar(Long id);
}
