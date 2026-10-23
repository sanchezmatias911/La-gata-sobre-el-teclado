package com.malb.gimnasio.dao;

import com.malb.gimnasio.modelo.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscripcionDAO extends JpaRepository<Inscripcion, Long> {
}
