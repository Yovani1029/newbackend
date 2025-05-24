package com.billetera.backend.infrastructure.Repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billetera.backend.domain.Entity.Factura;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findByEstado(String estado);

}
