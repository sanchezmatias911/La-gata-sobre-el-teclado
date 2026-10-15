package com.malb.gimnasio.dao;

import com.malb.gimnasio.modelo.ClaseProgramada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaseProgramadaDAO extends JpaRepository<ClaseProgramada, Long> {
    boolean existsByActividad_Id(Long actividadId); //comprueba si existe al menos una clase programada cuya actividad tenga ese ID.
}
