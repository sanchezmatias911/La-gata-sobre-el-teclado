package com.malb.gimnasio.dao;

import com.malb.gimnasio.modelo.usuario.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClienteDAO extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByDni(Long aLong);
}
