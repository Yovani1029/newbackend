package com.billetera.backend.application;

import com.billetera.backend.domain.Cuenta;
import com.billetera.backend.domain.Transaccion;
import com.billetera.backend.infrastructure.CuentaRepository;
import com.billetera.backend.infrastructure.TransaccionRepository;

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
