package com.malb.gimnasio;

import com.malb.gimnasio.dao.UsuarioDAO;
import com.malb.gimnasio.modelo.usuario.Administrador;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdministradorInicial implements ApplicationRunner {

    private final UsuarioDAO usuarioDAO;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String email;

    @Value("${admin.password}")
    private String password;

    @Value("${admin.dni}")
    private Long dni;

    @Value("${admin.nombre:Administrador}")
    private String nombre;

    @Value("${admin.telefono:1100000000}")
    private String telefono;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {

        if (email == null || email.isBlank()
                || password == null || password.isBlank()
                || dni == null) {

            // No es un error: en desarrollo el admin lo crea DatosIniciales y en los
            // tests no hace falta ninguno. Arrancar igual evita que se rompan los
            // @SpringBootTest cuando las variables ADMIN_* no están definidas.
            log.warn("ADMIN_EMAIL, ADMIN_PASSWORD o ADMIN_DNI no están definidos: "
                    + "no se crea ningún administrador desde la configuración.");
            return;
        }

        if (usuarioDAO.existsByEmailIgnoreCase(email.trim())) {
            log.info("El administrador inicial ya existe. No se crea otro.");
            return;
        }

        if (password.length() < 8) {
            throw new IllegalStateException(
                    "La contraseña del administrador debe tener al menos 8 caracteres."
            );
        }

        Administrador administrador = new Administrador(
                dni,
                email.trim().toLowerCase(),
                passwordEncoder.encode(password),
                nombre,
                telefono
        );

        usuarioDAO.save(administrador);

        log.info("Administrador inicial creado correctamente: {}", email);
    }
}
