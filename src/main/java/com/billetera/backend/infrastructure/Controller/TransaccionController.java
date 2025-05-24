package com.billetera.backend.infrastructure.Controller;

import com.billetera.backend.application.Services.TransaccionService;
import com.billetera.backend.domain.Entity.Transaccion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
@CrossOrigin(origins = "*")
public class TransaccionController {

    @Autowired
    private TransaccionService transaccionService;

    @GetMapping("/cuenta/{id}")
    public List<Transaccion> obtenerTransacciones(@PathVariable("id") Long cuentaId) {
        return transaccionService.obtenerTransaccionesPorCuenta(cuentaId);
    }
}
