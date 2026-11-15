package com.malb.gimnasio.service.impl;

import com.malb.gimnasio.dao.ProfesorDAO;
import com.malb.gimnasio.modelo.usuario.Profesor;
import com.malb.gimnasio.service.ProfesorService;
import com.malb.gimnasio.service.exception.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProfesorServiceImpl implements ProfesorService {
    private final ProfesorDAO profesorDAO;

    public ProfesorServiceImpl(ProfesorDAO profesorDAO) {
        this.profesorDAO = profesorDAO;
    }

    @Override
    public void crear(Profesor profesor) {
        if (profesor == null) { throw new IllegalArgumentException("no se puede crear algo nulo"); }

        profesorDAO.save(profesor);
    }

    @Override
    public Profesor recuperar(Long id) {
        if (id == null) { throw new IllegalArgumentException("no se puede recuperar algo nulo"); }

        return profesorDAO.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("no hay profesor con ese ID"));
    }
    @Override
    public Profesor recuperarPorDNI(Long dni) {
        if (dni == null) { throw new IllegalArgumentException("no se puede recuperar algo nulo"); }

        return profesorDAO.findByDni(dni)
                .orElseThrow(() -> new RecursoNoEncontradoException("no hay profesor con ese ID"));
    }

    @Override
    public List<Profesor> recuperarTodos() {
        return profesorDAO.findAll();
    }

    @Override
    public void eliminar(Long id) {
        if (id == null) { throw new IllegalArgumentException("no se puede recuperar algo nulo"); }

        /* todavia no se sabe si se debe de lanzar un error o borrar sus cursos. Se verá en US
        if (tieneCursos) {
            throw new RecuersoEnConlicto("No se puede eliminar el profesor"); //CONFLICT
        }*/

        Profesor profesor = recuperar(id);
        profesorDAO.delete(profesor);
    }

    @Override
    public void eliminarTodos() {
        profesorDAO.deleteAll();
    }
}
