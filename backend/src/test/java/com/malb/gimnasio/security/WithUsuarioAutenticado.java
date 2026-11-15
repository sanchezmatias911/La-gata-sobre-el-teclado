package com.malb.gimnasio.security;

import com.malb.gimnasio.modelo.usuario.Rol;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Reemplaza a @WithMockUser en los tests de controllers que usan
 * @AuthenticationPrincipal UsuarioAutenticado. @WithMockUser pone un
 * org.springframework.security.core.userdetails.User como principal, y Spring
 * lo resuelve como null para un parámetro de tipo UsuarioAutenticado.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithUsuarioAutenticadoFactory.class)
public @interface WithUsuarioAutenticado {
    long id() default 1L;
    String email() default "admin@test.com";
    Rol rol() default Rol.ADMIN;
}
