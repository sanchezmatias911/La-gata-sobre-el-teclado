package com.malb.gimnasio.service.impl;

import com.malb.gimnasio.dao.ClienteDAO;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.service.ClienteService;
import com.malb.gimnasio.service.exception.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {
    private final ClienteDAO clienteDAO;

    public ClienteServiceImpl(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    @Override
    public void crear(Cliente cliente) {
        clienteDAO.save(cliente);
    }

    @Override
    public Cliente recuperar(Long dni) {
        return clienteDAO.findByDni(dni).orElseThrow(() -> new RecursoNoEncontradoException("Cliente", dni));
    }

    @Override
    public List<Cliente> recuperarTodos() {
        return clienteDAO.findAll();
    }

    @Override
    public void eliminar(Long dni) {
        Cliente cliente = recuperar(dni);
        clienteDAO.delete(cliente);
    }
}
