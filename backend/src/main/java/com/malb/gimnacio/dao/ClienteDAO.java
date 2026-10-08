package com.malb.gimnacio.dao;

import com.malb.gimnacio.modelo.usuario.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteDAO extends JpaRepository<Cliente, Long> {
}
