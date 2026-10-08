package com.malb.gimnacio.dao;

import com.malb.gimnacio.modelo.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActividadDAO extends JpaRepository<Actividad, Long> {
}
