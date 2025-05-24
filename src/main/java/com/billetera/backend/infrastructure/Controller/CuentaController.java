package com.billetera.backend.infrastructure.Controller;

import com.billetera.backend.application.Services.CuentaService;
import com.billetera.backend.domain.Entity.Cuenta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping("/saldo")
    public ResponseEntity<?> consultarSaldo(@RequestParam String telefono) {
        try {
            Cuenta cuenta = cuentaService.obtenerCuentaPorTelefono(telefono);
            return ResponseEntity.ok().body("Saldo actual: $" + cuenta.getSaldo());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/transferir")
    public String transferirDinero(@RequestParam Long idRemitente,
            @RequestParam Long idDestinatario,
            @RequestParam BigDecimal monto) {

        Cuenta cuentaRemitente = cuentaService.obtenerCuentaPorUsuarioId(idRemitente);
        Cuenta cuentaDestinatario = cuentaService.obtenerCuentaPorUsuarioId(idDestinatario);

        if (cuentaRemitente == null || cuentaDestinatario == null) {
            return "Cuenta no encontrada";
        }

        boolean resultado = cuentaService.realizarTransferencia(cuentaRemitente, cuentaDestinatario, monto);

        if (resultado) {
            return "Transferencia realizada con éxito";
        } else {
            return "Saldo insuficiente";
        }
    }
}
