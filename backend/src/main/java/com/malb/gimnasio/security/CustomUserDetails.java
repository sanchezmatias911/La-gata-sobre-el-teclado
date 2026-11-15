package com.malb.gimnasio.security;

import com.malb.gimnasio.modelo.usuario.Administrador;
import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.modelo.usuario.Rol;
import com.malb.gimnasio.modelo.usuario.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;
    private final String password;
    private final Rol rol;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;

        if (usuario instanceof Cliente cliente) {
            this.password = cliente.getPassword();
            this.rol = Rol.CLIENTE;
        } else if (usuario instanceof Administrador administrador) {
            this.password = administrador.getPassword();
            this.rol = Rol.ADMIN;
        } else {
            throw new IllegalArgumentException(
                    "Este tipo de usuario no puede autenticarse"
            );
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + rol.name())
        );
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return usuario.getId();
    }

    public String getNombre() {
        return usuario.getNombre();
    }

    public String getEmail() {
        return usuario.getEmail();
    }

    public Rol getRol() {
        return rol;
    }
}