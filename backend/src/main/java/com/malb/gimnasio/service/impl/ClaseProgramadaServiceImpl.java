package com.malb.gimnasio.service.impl;

import com.malb.gimnasio.dao.ClaseProgramadaDAO;
import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.service.ClaseProgramadaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        claseProgramadaDAO.save(claseProgramada);
    }

    @Override
    public ClaseProgramada recuperar(Long id) {
        return claseProgramadaDAO.findById(id)
                .orElseThrow(() -> new NoSuchElementException("ClaseProgramada not found with id: " + id));
    }

    @Override
    public List<ClaseProgramada> recuperarTodos() {
        return claseProgramadaDAO.findAll();
    }

    @Override
    public void eliminar(Long id) {
        ClaseProgramada claseProgramada = recuperar(id);
        claseProgramadaDAO.delete(claseProgramada);
    }
}
