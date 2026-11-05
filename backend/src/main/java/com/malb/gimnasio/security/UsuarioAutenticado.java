package com.malb.gimnasio.security;

import com.malb.gimnasio.modelo.usuario.Rol;
import org.springframework.security.access.AccessDeniedException;

public record UsuarioAutenticado(Long id, String email, Rol rol) {

    /** El ADMIN puede operar sobre cualquier cliente; un CLIENTE solo sobre sí mismo. */
    public void exigirAccesoACliente(Long idCliente) {
        if (rol != Rol.ADMIN && !id.equals(idCliente)) {
            throw new AccessDeniedException("No podés operar sobre otro cliente.");
        }
    }
}
