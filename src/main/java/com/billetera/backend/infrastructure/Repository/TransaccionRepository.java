package com.billetera.backend.infrastructure.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billetera.backend.domain.Entity.Cuenta;
import com.billetera.backend.domain.Entity.Transaccion;

import java.util.List;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findByCuentaOrderByFechaTransaccionDesc(Cuenta cuenta);
}
