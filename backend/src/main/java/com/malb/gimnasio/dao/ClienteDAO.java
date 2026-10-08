package com.malb.gimnasio.dao;

import com.malb.gimnasio.modelo.usuario.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteDAO extends JpaRepository<Cliente, Long> {
}
