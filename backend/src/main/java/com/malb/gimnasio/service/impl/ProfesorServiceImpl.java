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
        profesorDAO.save(profesor);
    }

    @Override
    public Profesor recuperar(Long dni) {
        return profesorDAO.findById(dni)
                .orElseThrow(() -> new RecursoNoEncontradoException("Profesor", dni));
    }

    @Override
    public List<Profesor> recuperarTodos() {
        return profesorDAO.findAll();
    }

    @Override
    public void eliminar(Long dni) {
        Profesor profesor = recuperar(dni);
        profesorDAO.delete(profesor);
    }
}
