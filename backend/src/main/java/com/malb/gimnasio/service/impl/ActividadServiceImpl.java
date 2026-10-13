package com.malb.gimnasio.service.impl;

import com.malb.gimnasio.dao.ActividadDAO;
import com.malb.gimnasio.modelo.Actividad;
import com.malb.gimnasio.service.ActividadService;
import com.malb.gimnasio.service.exception.RecursoNoEncontradoException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Transactional
public class ActividadServiceImpl implements ActividadService {
    private final ActividadDAO actividadDAO;

    @Override
    public void crear(Actividad actividad) {
        actividadDAO.save(actividad);
    }

    @Override
    public Actividad recuperar(Long id) {
        return actividadDAO.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Actividad", id));
    }

    @Override
    public List<Actividad> recuperarTodos() {
        return actividadDAO.findAll();
    }

    @Override
    public void eliminar(Long id) {
        Actividad actividad = recuperar(id);
        actividadDAO.delete(actividad);
    }
}
