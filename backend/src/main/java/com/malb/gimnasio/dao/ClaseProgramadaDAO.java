package com.malb.gimnasio.dao;

import com.malb.gimnasio.modelo.ClaseProgramada;
import com.malb.gimnasio.modelo.Dia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;

@Repository
public interface ClaseProgramadaDAO extends JpaRepository<ClaseProgramada, Long> {
    boolean existsByActividad_Id(Long actividadId); //comprueba si existe al menos una clase programada cuya actividad tenga ese ID.

    @Query("""
            SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END
            FROM ClaseProgramada c
            WHERE c.dia = :dia
              AND c.horaInicio < :horaFin
              AND c.horaFin > :horaInicio
            """)
    boolean existeConflictoHorario(@Param("dia") Dia dia, @Param("horaInicio") LocalTime horaInicio, @Param("horaFin") LocalTime horaFin);
}
