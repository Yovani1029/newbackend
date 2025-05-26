package com.billetera.backend.infrastructure.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billetera.backend.domain.Entity.TicketSoporte;

import java.util.List;

public interface TicketSoporteRepository extends JpaRepository<TicketSoporte, Long> {
    List<TicketSoporte> findByCorreoContacto(String correo);

    List<TicketSoporte> findByUsuarioId(Long usuarioId); // Si hay relación con Usuario
}