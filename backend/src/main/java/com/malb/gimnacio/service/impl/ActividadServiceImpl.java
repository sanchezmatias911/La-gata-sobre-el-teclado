package com.malb.gimnacio.service.impl;

import com.malb.gimnacio.dao.ActividadDAO;
import com.malb.gimnacio.modelo.Actividad;
import com.malb.gimnacio.service.ActividadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ActividadServiceImpl implements ActividadService {
    private final ActividadDAO actividadDAO;

    public ActividadServiceImpl(ActividadDAO actividadDAO) {
        this.actividadDAO = actividadDAO;
    }

    @Override
    public void crear(Actividad actividad) {
        actividadDAO.save(actividad);
    }

    @Override
    public Actividad recuperar(Long id) {
        return actividadDAO.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Actividad not found with id: " + id));
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
