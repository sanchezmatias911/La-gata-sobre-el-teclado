package com.malb.gimnacio.dao;

import com.malb.gimnacio.modelo.usuario.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfesorDAO extends JpaRepository<Profesor, Long> {
}
