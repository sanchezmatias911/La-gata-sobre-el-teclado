package com.malb.gimnasio.dao;

import com.malb.gimnasio.modelo.ClaseProgramada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaseProgramadaDAO extends JpaRepository<ClaseProgramada, Long> {
}
