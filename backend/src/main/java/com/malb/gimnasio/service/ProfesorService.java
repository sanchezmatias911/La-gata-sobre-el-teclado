package com.malb.gimnasio.service;

import com.malb.gimnasio.modelo.usuario.Profesor;

import java.util.List;

public interface ProfesorService {
    void crear(Profesor profesor);
    Profesor recuperar(Long id);
    Profesor recuperarPorDNI(Long dni);
    List<Profesor> recuperarTodos();
    void eliminar(Long dni);
    void eliminarTodos();
}
