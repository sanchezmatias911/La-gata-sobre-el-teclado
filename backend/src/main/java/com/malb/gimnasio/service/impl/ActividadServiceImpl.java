package com.malb.gimnasio.service.impl;

import com.malb.gimnasio.dao.ActividadDAO;
import com.malb.gimnasio.dao.ClaseProgramadaDAO;
import com.malb.gimnasio.modelo.Actividad;
import com.malb.gimnasio.service.ActividadService;
import com.malb.gimnasio.service.exception.RecursoEnUsoException;
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
    private final ClaseProgramadaDAO claseProgramadaDAO;

    @Override
    public void crear(Actividad actividad) {
        if (actividad == null) { throw new IllegalArgumentException("no se puede crear algo nulo"); }

        actividadDAO.save(actividad);
    }

    @Override
    public Actividad recuperar(Long id) {
        if (id == null) { throw new IllegalArgumentException("no se puede recuperar algo nulo"); }

        return actividadDAO.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("actividad con ese id"));
    }

    @Override
    public List<Actividad> recuperarTodos() {
        return actividadDAO.findAll();
    }

    @Override
    public void eliminar(Long id) {
        if (id == null) { throw new IllegalArgumentException("no se puede recuperar algo nulo"); }

        if (claseProgramadaDAO.existsByActividad_Id(id)) {
            throw new RecursoEnUsoException(
                    "No se puede eliminar la actividad porque está asociada a una o más clases programadas."
            );
        }

        Actividad actividad = recuperar(id);
        actividadDAO.delete(actividad);
    }
}
