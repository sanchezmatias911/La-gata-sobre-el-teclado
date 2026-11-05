package com.malb.gimnasio.security;

import com.malb.gimnasio.dao.UsuarioDAO;
import com.malb.gimnasio.modelo.usuario.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioDAO usuarioDAO;

    public CustomUserDetailsService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioDAO.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "No existe un usuario con ese email"
                        )
                );

        try {
            return new CustomUserDetails(usuario);
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException(
                    "El usuario no tiene permitido iniciar sesión"
            );
        }
    }
}