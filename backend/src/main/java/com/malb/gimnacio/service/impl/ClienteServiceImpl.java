package com.malb.gimnacio.service.impl;

import com.malb.gimnacio.dao.ClienteDAO;
import com.malb.gimnacio.modelo.usuario.Cliente;
import com.malb.gimnacio.service.ClienteService;
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
        return clienteDAO.findById(dni)
                .orElseThrow(() -> new NoSuchElementException("Cliente not found with dni: " + dni));
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
