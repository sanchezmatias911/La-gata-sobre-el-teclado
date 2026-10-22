package com.malb.gimnasio.service.impl;

import com.malb.gimnasio.dao.ClaseProgramadaDAO;
import com.malb.gimnasio.dao.ClienteDAO;
import com.malb.gimnasio.dao.InscripcionDAO;
import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.service.ClaseProgramadaService;
import com.malb.gimnasio.service.ClienteService;
import com.malb.gimnasio.service.InscripcionService;
import com.malb.gimnasio.service.exception.InscripcionNoPermitidaException;
import com.malb.gimnasio.service.exception.RecursoDuplicadoException;
import com.malb.gimnasio.service.exception.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
public class InscripcionServiceImpl implements InscripcionService {
    private final InscripcionDAO inscripcionDAO;
    private final ClaseProgramadaDAO claseProgramadaDAO;
    private final ClienteDAO clienteDAO;

    @Override
    public Inscripcion crear(Long idCliente, Long idClase) {
        ClaseProgramada clase = claseProgramadaDAO.findById(idClase).orElseThrow(() -> new RecursoNoEncontradoException("clase con ese dni"));
        Cliente cliente = clienteDAO.findById(idCliente).orElseThrow(() -> new RecursoNoEncontradoException("cliente con ese ID"));

        if (!clase.puedeRecibirInscripciones()) {
            throw new InscripcionNoPermitidaException("La clase no está disponible para nuevas inscripciones");
        }

        if (clase.tieneInscripto(cliente)) {
            throw new RecursoDuplicadoException("El cliente ya está inscripto en esta clase");
        }

        Inscripcion inscripcion = new Inscripcion(cliente, clase);

        cliente.agregarInscripcion(inscripcion);
        clase.agregarInscripcion(inscripcion);
        clienteDAO.save(cliente);
        claseProgramadaDAO.save(clase);

        return inscripcionDAO.save(inscripcion);
    }

    @Override
    public Inscripcion recuperar(Long id) {
        if (id == null) { throw new IllegalArgumentException("no se puede recuperar algo nulo"); }

        return inscripcionDAO.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("inscripcion con ese id"));
    }

    @Override
    public List<Inscripcion> recuperarTodos() {
        return inscripcionDAO.findAll();
    }

    @Override
    public void eliminar(Long id) {
        if (id == null) { throw new IllegalArgumentException("no se puede recuperar algo nulo"); }

        /* Creo que debería de eliminarse de forma "tanquila"
        ver si implementarlo en su US
        */

        Inscripcion inscripcion = recuperar(id);
        inscripcionDAO.delete(inscripcion);
    }
    @Override
    public void eliminarTodos() {
        inscripcionDAO.deleteAll();
    }
}
