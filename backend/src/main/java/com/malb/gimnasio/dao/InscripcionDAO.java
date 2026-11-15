package com.malb.gimnasio.dao;

import com.malb.gimnasio.modelo.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscripcionDAO extends JpaRepository<Inscripcion, Long> {
    // detecta inscripciones existentes en persistencia, aunque la colección esté desactualizada.
    boolean existsByCliente_IdAndClaseProgramada_Id(Long clienteId, Long claseProgramadaId);
}
