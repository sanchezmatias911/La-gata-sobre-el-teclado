package com.malb.gimnacio.service;

import com.malb.gimnacio.modelo.usuario.Profesor;

import java.util.List;

public interface ProfesorService {
    void crear(Profesor profesor);
    Profesor recuperar(Long dni);
    List<Profesor> recuperarTodos();
    void eliminar(Long dni);
}
