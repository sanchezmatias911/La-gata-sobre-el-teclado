package com.malb.gimnasio.service.impl;

import com.malb.gimnasio.dao.InscripcionDAO;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.service.InscripcionService;
import com.malb.gimnasio.service.exception.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class InscripcionServiceImpl implements InscripcionService {
    private final InscripcionDAO inscripcionDAO;

    public InscripcionServiceImpl(InscripcionDAO inscripcionDAO) {
        this.inscripcionDAO = inscripcionDAO;
    }

    @Override
    public void crear(Inscripcion inscripcion) {
        inscripcionDAO.save(inscripcion);
    }

    @Override
    public Inscripcion recuperar(Long id) {
        return inscripcionDAO.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Inscripcion", id));
    }

    @Override
    public List<Inscripcion> recuperarTodos() {
        return inscripcionDAO.findAll();
    }

    @Override
    public void eliminar(Long id) {
        Inscripcion inscripcion = recuperar(id);
        inscripcionDAO.delete(inscripcion);
    }
}
