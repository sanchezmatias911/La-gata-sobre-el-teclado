package com.malb.gimnasio.modelo.usuario;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@DiscriminatorValue("Administrador")
@NoArgsConstructor
@Getter
@Setter
public class Administrador extends Usuario {
    @NotBlank
    private String password; // siempre hasheada con BCrypt

    public Administrador(@NonNull Long dni, @NonNull String email, @NonNull String passwordHasheada, @NonNull String nombre, @NonNull String telefono) {
        super(dni, email, nombre, telefono);
        this.password = passwordHasheada;
    }
}