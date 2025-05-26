package com.billetera.backend.domain.ports;

import com.billetera.backend.domain.Entity.TicketSoporte;

public interface TicketSoporteRepository {
    TicketSoporte save(TicketSoporte ticket);
}