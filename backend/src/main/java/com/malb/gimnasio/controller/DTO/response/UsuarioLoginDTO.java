
package com.malb.gimnasio.controller.DTO.response;

import com.malb.gimnasio.modelo.usuario.Rol;
import com.malb.gimnasio.security.CustomUserDetails;

public record UsuarioLoginDTO(
        Long id,
        String nombre,
        String email,
        Rol rol
) {
    public static UsuarioLoginDTO desdeUsuario(CustomUserDetails usuario) {
        return new UsuarioLoginDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }
}
