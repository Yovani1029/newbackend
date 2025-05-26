package com.billetera.backend.application.Services;

import com.billetera.backend.domain.Entity.Cuenta;
import com.billetera.backend.domain.Entity.Transaccion;
import com.billetera.backend.domain.Entity.Usuario;
import com.billetera.backend.infrastructure.Repository.CuentaRepository;
import com.billetera.backend.infrastructure.Repository.TransaccionRepository;
import com.billetera.backend.infrastructure.Repository.UsuarioRepository;
import com.billetera.backend.infrastructure.dto.DepositoResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DepositoService {
    private final UsuarioRepository usuarioRepository;
    private final CuentaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;

    public DepositoService(
        UsuarioRepository usuarioRepository,
        CuentaRepository cuentaRepository,
        TransaccionRepository transaccionRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.cuentaRepository = cuentaRepository;
        this.transaccionRepository = transaccionRepository;
    }

    @Transactional
    public DepositoResponse realizarDeposito(String telefono, BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }

        Usuario usuario = usuarioRepository.findByTelefono(telefono)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Cuenta cuenta = usuario.getCuenta();
        BigDecimal saldoAnterior = cuenta.getSaldo();
        cuenta.setSaldo(saldoAnterior.add(monto));
        cuentaRepository.save(cuenta);

        Transaccion transaccion = new Transaccion();
        transaccion.setCuenta(cuenta);
        transaccion.setMonto(monto);
        transaccion.setTipoTransaccion("DEPOSITO");
        transaccion.setDescripcion("Depósito realizado");
        transaccion.setEstado("COMPLETADO");
        transaccionRepository.save(transaccion);

        return new DepositoResponse(
            true,
            "Depósito realizado con éxito",
            telefono,
            monto,
            cuenta.getSaldo()
        );
    }
}