package com.malb.gimnasio.dao;

import com.malb.gimnasio.modelo.usuario.Cliente;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteDAO extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByDni(Long dni);

    @Modifying
    @Transactional
    @Query(value = """
    DELETE FROM usuario_mis_inscripciones
    WHERE cliente_id = :idCliente
      AND mis_inscripciones_id = :idInscripcion
    """, nativeQuery = true)
    void eliminarSuscripcion(@Param("idCliente") Long idCliente, @Param("idInscripcion") Long idInscripcion);
}
