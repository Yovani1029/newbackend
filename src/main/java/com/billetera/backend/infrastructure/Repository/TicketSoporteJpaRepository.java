package com.billetera.backend.infrastructure.Repository;


import com.billetera.backend.domain.Entity.TicketSoporte;
import com.billetera.backend.domain.ports.TicketSoporteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketSoporteJpaRepository extends 
    JpaRepository<TicketSoporte, Long>, 
    TicketSoporteRepository {
}