package com.billetera.backend.application.Services;

import com.billetera.backend.domain.Entity.Cuenta;
import com.billetera.backend.domain.Entity.Transaccion;
import com.billetera.backend.infrastructure.Repository.CuentaRepository;
import com.billetera.backend.infrastructure.Repository.TransaccionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private CuentaRepository cuentaRepository;

    public List<Transaccion> obtenerTransaccionesPorCuenta(Long cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
        return transaccionRepository.findByCuentaOrderByFechaTransaccionDesc(cuenta);
    }
}
