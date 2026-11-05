package com.malb.gimnasio.service;

import com.malb.gimnasio.controller.DTO.request.CrearLoginDTO;
import com.malb.gimnasio.controller.DTO.request.RegistroClienteDTO;
import com.malb.gimnasio.controller.DTO.response.LoginDTO;

public interface AuthenticationService {
    void registrar(RegistroClienteDTO dto);

    LoginDTO login(CrearLoginDTO dto);
}
