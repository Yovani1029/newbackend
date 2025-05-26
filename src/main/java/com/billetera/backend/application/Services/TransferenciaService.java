package com.billetera.backend.application.Services;

import com.billetera.backend.domain.Entity.Cuenta;
import com.billetera.backend.domain.Entity.Transaccion;
import com.billetera.backend.infrastructure.Repository.TransaccionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferenciaService {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Transactional
    public String transferirPorTelefono(String telefonoRemitente, String telefonoDestinatario, BigDecimal monto) {
        if (telefonoRemitente.equals(telefonoDestinatario)) {
            throw new IllegalArgumentException("No puedes transferir a tu mismo número");
        }

        Cuenta cuentaRemitente = cuentaService.obtenerCuentaPorTelefono(telefonoRemitente);
        Cuenta cuentaDestino = cuentaService.obtenerCuentaPorTelefono(telefonoDestinatario);
        cuentaRemitente.setSaldo(cuentaRemitente.getSaldo().subtract(monto));
        cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(monto));
        cuentaService.actualizarSaldo(cuentaRemitente);
        cuentaService.actualizarSaldo(cuentaDestino);

        if (cuentaRemitente == null || cuentaDestino == null) {
            throw new IllegalArgumentException("No se encontró alguna de las cuentas");
        }

        if (cuentaRemitente.getSaldo().compareTo(monto) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar la transferencia");
        }

        String nombreRemitente = cuentaRemitente.getUsuario().getNombre() + " "
                + cuentaRemitente.getUsuario().getApellido();
        String nombreDestinatario = cuentaDestino.getUsuario().getNombre() + " "
                + cuentaDestino.getUsuario().getApellido();
        registrarTransaccion(cuentaRemitente, monto.negate(), "TRANSFERENCIA_ENVIADA",
                "Transferencia a " + nombreDestinatario + " (" + telefonoDestinatario + ")");
        registrarTransaccion(cuentaDestino, monto, "TRANSFERENCIA_RECIBIDA",
                "Transferencia de " + nombreRemitente + " (" + telefonoRemitente + ")");
        transaccionRepository.flush();

        return String.format(
                "Transferencia exitosa a %s por $%,.2f %ncon numero: %s",
                nombreDestinatario, monto, telefonoDestinatario);
    }

    private void registrarTransaccion(Cuenta cuenta, BigDecimal monto, String tipo, String descripcion) {
        Transaccion transaccion = new Transaccion();
        transaccion.setCuenta(cuenta);
        transaccion.setMonto(monto);
        transaccion.setTipoTransaccion(tipo);
        transaccion.setDescripcion(descripcion);
        transaccionRepository.save(transaccion);
    }
}