package com.billetera.backend.infrastructure.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.billetera.backend.domain.Entity.Cuenta;
import com.billetera.backend.domain.Entity.Transaccion;

import java.util.List;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findByCuentaOrderByFechaTransaccionDesc(Cuenta cuenta);

    // ✅ Método para borrar todas las transacciones de una cuenta por ID
    void deleteByCuentaId(Long cuentaId);
}
