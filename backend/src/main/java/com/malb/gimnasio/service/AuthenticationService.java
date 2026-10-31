package com.malb.gimnasio.service;

import com.malb.gimnasio.controller.DTO.request.RegistroClienteDTO;

public interface AuthenticationService {
    public void registrar(RegistroClienteDTO dto);
}
