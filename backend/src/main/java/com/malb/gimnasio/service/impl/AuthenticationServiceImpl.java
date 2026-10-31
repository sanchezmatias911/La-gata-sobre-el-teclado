package com.malb.gimnasio.service.impl;

import com.malb.gimnasio.controller.DTO.request.RegistroClienteDTO;
import com.malb.gimnasio.dao.ClienteDAO;
import com.malb.gimnasio.modelo.exception.RecursoDuplicadoException;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.service.AuthenticationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private final ClienteDAO clienteDAO;

    public AuthenticationServiceImpl(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public void registrar(RegistroClienteDTO dto) {
        String mail = dto.mail().trim().toLowerCase(Locale.ROOT);

        if (clienteDAO.existsByEmail(mail)) {
            throw new RecursoDuplicadoException("Ya existe un usuario registrado con ese mail");
        }

        String nombre = capitalizar(dto.nombre());
        String apellido = capitalizar(dto.apellido());

        Cliente cliente = new Cliente(
                dto.dni(),
                mail,
                dto.contraseña(),
                nombre + " " + apellido,
                dto.telefono().trim()
        );

        clienteDAO.save(cliente);
    }

    private String capitalizar(String texto) {
        texto = texto.trim().toLowerCase(Locale.ROOT);

        return texto.substring(0, 1).toUpperCase(Locale.ROOT) + texto.substring(1);
    }
}
