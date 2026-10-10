package com.malb.gimnasio.dao;

import com.malb.gimnasio.modelo.usuario.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesorDAO extends JpaRepository<Profesor, Long> {
}
