package com.malb.gimnacio.dao;

import com.malb.gimnacio.modelo.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionDAO extends JpaRepository<Inscripcion, Long> {
}
