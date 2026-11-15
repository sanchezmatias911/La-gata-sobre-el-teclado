package com.malb.gimnasio.security;

import com.malb.gimnasio.modelo.usuario.Rol;
import com.malb.gimnasio.security.error.MensajesDeSeguridad;
import org.springframework.security.access.AccessDeniedException;

public record UsuarioAutenticado(Long id, String email, Rol rol) {

    public static UsuarioAutenticado desde(CustomUserDetails usuario) {
        return new UsuarioAutenticado(usuario.getId(), usuario.getEmail(), usuario.getRol());
    }

    public void exigirAccesoACliente(Long idCliente) {
        if (!puedeOperarSobre(idCliente)) {
            throw new AccessDeniedException(MensajesDeSeguridad.OPERAR_SOBRE_OTRO_CLIENTE);
        }
    }

    private boolean puedeOperarSobre(Long idCliente) {
        return esAdmin() || esElCliente(idCliente);
    }

    private boolean esAdmin() {
        return rol == Rol.ADMIN;
    }

    private boolean esElCliente(Long idCliente) {
        return id.equals(idCliente);
    }
}