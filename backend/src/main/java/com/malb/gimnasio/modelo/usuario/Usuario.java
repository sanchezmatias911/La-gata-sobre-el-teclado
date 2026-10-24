package com.malb.gimnasio.modelo.usuario;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;



@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
@Getter
@Setter @NoArgsConstructor @ToString
@Entity
public abstract class Usuario{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull (message = "El DNI no puede estar vacío.")
    @Column(unique = true)
    private Long dni;

    @NotBlank (message = "El email no puede estar vacío.")
    private String email;

    @NotBlank (message = "La contraseña no puede estar vacía.")
    @Size(min = 8, message = "La contraseña debe tener al menos  8 caracteres.")
    private String password;

    @NotBlank (message = "El nombre no puede estar vacío.")
    private String nombre;

    @Size(min = 10, message = "El número de teléfono está incompleto.")
    private String telefono;

    public Usuario (@NonNull Long dni, @NonNull String email, @NonNull String password, @NonNull String nombre, @NonNull String telefono){
        this.dni = dni;
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.telefono = telefono;
    }


}
