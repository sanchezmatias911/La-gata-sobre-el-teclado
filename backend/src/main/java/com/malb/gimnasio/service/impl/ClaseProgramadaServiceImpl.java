package com.malb.gimnasio.service.impl;

import com.malb.gimnasio.dao.ClaseProgramadaDAO;
import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.service.ClaseProgramadaService;
import com.malb.gimnasio.service.exception.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ClaseProgramadaServiceImpl implements ClaseProgramadaService {
    private final ClaseProgramadaDAO claseProgramadaDAO;

    public ClaseProgramadaServiceImpl(ClaseProgramadaDAO claseProgramadaDAO) {
        this.claseProgramadaDAO = claseProgramadaDAO;
    }

    @Override
    public void crear(ClaseProgramada claseProgramada) {
        if (claseProgramada == null) { throw new IllegalArgumentException("no se puede crear algo nulo"); }
        claseProgramadaDAO.save(claseProgramada);
    }

    @Override
    public ClaseProgramada recuperar(Long id) {
        if (id == null) { throw new IllegalArgumentException("no se puede recuperar algo nulo"); }

        return claseProgramadaDAO.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("clase con ese id"));
    }

    @Override
    public List<ClaseProgramada> recuperarTodos() {
        return claseProgramadaDAO.findAll();
    }

    @Override
    public void eliminar(Long id) {
        if (id == null) { throw new IllegalArgumentException("no se puede recuperar algo nulo"); }

        ClaseProgramada claseProgramada = recuperar(id);

        /* posible flujo, el objetivo es que no tire un error por eliminar cuando sigue las relaciones

        // 1. Obtener los destinatarios antes de eliminar la clase.
        List<Inscripcion> inscripciones = new ArrayList<>(clase.getInscripciones());

        // 2. Registrar las notificaciones antes de perder los datos.
        notificacionService.registrarCancelacion(clase, inscripciones);

        // 3. Desvincular las relaciones.
        clase.setProfesor(null);
        clase.setActividad(null);*/

        claseProgramadaDAO.delete(claseProgramada);
    }
}
