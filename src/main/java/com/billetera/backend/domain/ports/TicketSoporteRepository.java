package com.billetera.backend.domain.ports;

import java.util.List;
import com.billetera.backend.domain.Entity.TicketSoporte;

public interface TicketSoporteRepository {
    TicketSoporte save(TicketSoporte ticket);
    List<TicketSoporte> findByUsuarioId(Long usuarioId);
}
