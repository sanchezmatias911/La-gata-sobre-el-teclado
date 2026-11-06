package com.malb.gimnasio.controller.DTO.request;

import com.malb.gimnasio.modelo.usuario.Cliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearClienteDTO (
    @NotNull
    Long dni,

    @NotBlank
    @Email
    String email,

    @NotBlank
    @Size(min = 8, message = "La password debe tener al menos 8 caracteres")
    String password,

    @NotBlank
    String nombre,

    @NotBlank
    String telefono
) {
        public Cliente aModelo() {
            return new Cliente(
                    this.dni(),
                    this.email(),
                    this.password(),
                    this.nombre(),
                    this.telefono()
            );
        }
}
