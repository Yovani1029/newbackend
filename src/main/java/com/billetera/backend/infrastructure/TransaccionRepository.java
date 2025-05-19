package com.billetera.backend.infrastructure;

import com.billetera.backend.domain.Cuenta;
import com.billetera.backend.domain.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findByCuentaOrderByFechaTransaccionDesc(Cuenta cuenta);
}
