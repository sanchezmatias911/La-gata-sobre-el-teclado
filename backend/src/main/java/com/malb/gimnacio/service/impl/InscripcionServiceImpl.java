package com.malb.gimnacio.service.impl;

import com.malb.gimnacio.dao.InscripcionDAO;
import com.malb.gimnacio.modelo.Inscripcion;
import com.malb.gimnacio.service.InscripcionService;
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
                .orElseThrow(() -> new NoSuchElementException("Inscripcion not found with id: " + id));
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
