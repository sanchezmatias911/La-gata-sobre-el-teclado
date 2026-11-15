package com.malb.gimnasio.controller.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistroClienteDTO(@NotBlank(message = "El nombre es obligatorio")
                                 String nombre,

                                 @NotBlank(message = "El apellido es obligatorio")
                                 String apellido,

                                 @NotBlank(message = "El mail es obligatorio")
                                 @Email(message = "El mail no es válido")
                                 String mail,

                                 @NotBlank(message = "La password es obligatoria")
                                 @Size(min = 8, message = "La password debe tener al menos 8 caracteres")
                                 String password,

                                 @NotBlank(message = "El teléfono es obligatorio")
                                 @Size(min = 10, message = "El teléfono debe tener al menos 10 caracteres")
                                 String telefono,

                                 @NotNull(message = "El DNI es obligatorio")
                                 Long dni) {
}
