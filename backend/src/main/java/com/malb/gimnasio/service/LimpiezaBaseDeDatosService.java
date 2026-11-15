package com.malb.gimnasio.service;

import com.malb.gimnasio.dao.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//lo quise hacer bonito (´｡• ᵕ •｡`)
@Service
@RequiredArgsConstructor
public class LimpiezaBaseDeDatosService {
    private final InscripcionDAO inscripcionDAO;
    private final ClaseProgramadaDAO claseProgramadaDAO;
    private final ClienteDAO clienteDAO;
    private final ProfesorDAO profesorDAO;
    private final ActividadDAO actividadDAO;

    @Transactional
    public void limpiarBaseDeDatos() {
        inscripcionDAO.deleteAll();
        claseProgramadaDAO.deleteAll();
        clienteDAO.deleteAll();
        profesorDAO.deleteAll();
        actividadDAO.deleteAll();
    }
}
