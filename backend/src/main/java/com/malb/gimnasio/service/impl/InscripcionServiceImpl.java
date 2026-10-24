package com.malb.gimnasio.service.impl;

import com.malb.gimnasio.dao.ClaseProgramadaDAO;
import com.malb.gimnasio.dao.ClienteDAO;
import com.malb.gimnasio.dao.InscripcionDAO;
import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.service.InscripcionService;
import com.malb.gimnasio.service.exception.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class InscripcionServiceImpl implements InscripcionService {
    private final InscripcionDAO inscripcionDAO;
    private final ClaseProgramadaDAO claseProgramadaDAO;
    private final ClienteDAO clienteDAO;

    @Override
    public Inscripcion crear(Cliente cliente, ClaseProgramada clase) {
        //se asume que ya están persistidas y no es nulo, al ser utilizada por el controller

        Inscripcion inscripcion = new Inscripcion(cliente, clase);

        cliente.agregarInscripcion(inscripcion); //se podría hacer que luego el cliente mande el otro mensaje de abajo.
        clase.agregarInscripcion(inscripcion);

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

        /* Aca podes usar los metodos que cree en clase y cliente, asi no es necesario hacer el metodo eliminarSuscripcion de cliente
        */

        Inscripcion inscripcion = recuperar(id);
        inscripcionDAO.delete(inscripcion);
    }
    @Override
    public void eliminarTodos() {
        inscripcionDAO.deleteAll();
    }
}
