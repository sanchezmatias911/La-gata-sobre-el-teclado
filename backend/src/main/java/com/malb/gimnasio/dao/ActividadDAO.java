package com.malb.gimnasio.dao;

import com.malb.gimnasio.modelo.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActividadDAO extends JpaRepository<Actividad, Long> {
}
