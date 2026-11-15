package com.malb.gimnasio.service.impl;

import com.malb.gimnasio.dao.ClienteDAO;
import com.malb.gimnasio.modelo.Inscripcion;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.service.ClienteService;
import com.malb.gimnasio.service.exception.RecursoNoEncontradoException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {
    private final ClienteDAO clienteDAO;
    private final PasswordEncoder passwordEncoder;

    public ClienteServiceImpl(ClienteDAO clienteDAO, PasswordEncoder passwordEncoder) {
        this.clienteDAO = clienteDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void crear(Cliente cliente) {
        if (cliente == null) { throw new IllegalArgumentException("no se puede crear algo nulo"); }

        // El cliente llega con la contraseña en texto plano y se guarda hasheada (BCrypt).
        // Quien llame a crear() NO debe encriptarla antes, o quedaría doble hasheada.
        cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));

        clienteDAO.save(cliente);
    }

    @Override
    public Cliente recuperarPorDNI(Long dni) {
        if (dni == null) { throw new IllegalArgumentException("no se puede recuperar algo nulo"); }

        return clienteDAO.findByDni(dni)
                .orElseThrow(() -> new RecursoNoEncontradoException("cliente con ese dni"));
    }
    @Override
    public Cliente recuperar(Long id) {
        if (id == null) { throw new IllegalArgumentException("no se puede recuperar algo nulo"); }

        return clienteDAO.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("cliente con ese id"));
    }

    @Override
    public List<Cliente> recuperarTodos() {
        return clienteDAO.findAll();
    }

    @Override
    public void eliminar(Long id) {
        if (id == null) { throw new IllegalArgumentException("no se puede recuperar algo nulo"); }

        /* Se debería eliminar "tranquilamente" junto a sus incrpciones
        * se verá en su US
        * */

        clienteDAO.delete(recuperar(id));
    }
    @Override
    public void eliminarTodos() {
        clienteDAO.deleteAll();
    }

    @Override
    public void eliminarSuscripcion(Long id, Long idInscripcion, Long idClase) {
        if (id == null) { throw new IllegalArgumentException("no se puede recuperar algo nulo"); }
        Cliente cliente = recuperar(id);
        cliente.getMisInscripciones().removeIf(
                inscripcion -> inscripcion.getId().equals(idInscripcion));
    }

}
