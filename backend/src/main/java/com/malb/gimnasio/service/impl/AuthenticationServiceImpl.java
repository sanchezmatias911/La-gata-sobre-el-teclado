package com.malb.gimnasio.service.impl;

import com.malb.gimnasio.controller.DTO.request.CrearLoginDTO;
import com.malb.gimnasio.controller.DTO.request.RegistroClienteDTO;
import com.malb.gimnasio.controller.DTO.response.LoginDTO;
import com.malb.gimnasio.dao.ClienteDAO;
import com.malb.gimnasio.modelo.exception.RecursoDuplicadoException;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.service.AuthenticationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.malb.gimnasio.dao.UsuarioDAO;
import com.malb.gimnasio.security.CustomUserDetails;
import com.malb.gimnasio.security.JwtService;
import com.malb.gimnasio.controller.DTO.response.UsuarioLoginDTO;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Locale;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private final ClienteDAO clienteDAO;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationServiceImpl(ClienteDAO clienteDAO, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.clienteDAO = clienteDAO;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void registrar(RegistroClienteDTO dto) {
        String mail = dto.mail().trim().toLowerCase(Locale.ROOT);

        if (clienteDAO.existsByEmail(mail)) {
            throw new RecursoDuplicadoException("Ya existe un usuario registrado con ese mail");
        }

        String nombre = capitalizar(dto.nombre());
        String apellido = capitalizar(dto.apellido());

        Cliente cliente = new Cliente(dto.dni(), mail, passwordEncoder.encode(dto.password()), nombre + " " + apellido, dto.telefono().trim());

        clienteDAO.save(cliente);
    }

    @Override
    public LoginDTO login(CrearLoginDTO dto) {

        String email = dto.email().trim().toLowerCase(Locale.ROOT);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, dto.password()));

        CustomUserDetails usuario = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtService.generarToken(usuario);

        return new LoginDTO(token, "Bearer", jwtService.getExpiracionSegundos(), UsuarioLoginDTO.desdeUsuario(usuario));
    }


    private String capitalizar(String texto) {
        texto = texto.trim().toLowerCase(Locale.ROOT);

        return texto.substring(0, 1).toUpperCase(Locale.ROOT) + texto.substring(1);
    }
}
