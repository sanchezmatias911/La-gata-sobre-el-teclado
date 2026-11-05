package com.malb.gimnasio.controller.REST;

import com.malb.gimnasio.controller.DTO.request.CrearLoginDTO;
import com.malb.gimnasio.controller.DTO.request.RegistroClienteDTO;
import com.malb.gimnasio.controller.DTO.response.LoginDTO;
import com.malb.gimnasio.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationREST {
    private final AuthenticationService authService;

    public AuthenticationREST(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    public ResponseEntity<Void> registrar(@Valid @RequestBody RegistroClienteDTO dto) {
        authService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@Valid @RequestBody CrearLoginDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
