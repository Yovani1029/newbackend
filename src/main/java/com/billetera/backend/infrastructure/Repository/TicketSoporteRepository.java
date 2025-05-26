package com.billetera.backend.infrastructure.Repository;

import com.billetera.backend.domain.Entity.TicketSoporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketSoporteRepository extends JpaRepository<TicketSoporte, Long> {
    List<TicketSoporte> findByUsuarioId(Long usuarioId);
}
