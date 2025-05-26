package com.billetera.backend.application.Services;

import com.billetera.backend.domain.Entity.Cuenta;
import com.billetera.backend.infrastructure.Repository.CuentaRepository;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    public Cuenta obtenerCuentaPorUsuarioId(Long usuarioId) {
        return cuentaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada para el usuario ID: " + usuarioId));
    }

    public Cuenta obtenerCuentaPorTelefono(String telefono) {
        return cuentaRepository.findByUsuarioTelefono(telefono)
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada para el teléfono: " + telefono));
    }

    public Optional<Cuenta> obtenerCuentaPorId(Long id) {
        return cuentaRepository.findById(id);
    }

    public boolean realizarTransferencia(Cuenta remitente, Cuenta destinatario, BigDecimal monto) {
        if (remitente.getSaldo().compareTo(monto) < 0) {
            return false;
        }

        remitente.setSaldo(remitente.getSaldo().subtract(monto));
        destinatario.setSaldo(destinatario.getSaldo().add(monto));

        this.actualizarSaldo(remitente);
        this.actualizarSaldo(destinatario);

        return true;
    }

    public void actualizarSaldo(Cuenta cuenta) {
        cuentaRepository.saveAndFlush(cuenta);
    }
}