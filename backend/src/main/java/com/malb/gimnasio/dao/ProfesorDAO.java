package com.malb.gimnasio.dao;

import com.malb.gimnasio.modelo.usuario.Cliente;
import com.malb.gimnasio.modelo.usuario.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfesorDAO extends JpaRepository<Profesor, Long> {
    Optional<Profesor> findByDni(Long dni); //si se quiere trabajar con dni para recuperar
}
