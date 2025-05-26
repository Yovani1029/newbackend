package com.billetera.backend.infrastructure.Controller;

import com.billetera.backend.application.Services.FacturaService;
import com.billetera.backend.domain.Entity.Factura;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@CrossOrigin(origins = "http://localhost:8100")
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @PostMapping("/usuario/{usuarioId}")
    public Factura crearFactura(@PathVariable Long usuarioId, @RequestBody Factura factura) {
        return facturaService.crearFactura(factura, usuarioId);
    }

    @PutMapping("/{id}")
    public Factura actualizar(@PathVariable Long id, @RequestBody Factura factura) {
        return facturaService.actualizarFactura(id, factura);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        facturaService.eliminarFactura(id);
    }

    @GetMapping("/pendientes")
    public List<Factura> obtenerFacturasPendientes() {
        return facturaService.obtenerFacturasPendientes();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Factura> obtenerFacturas(@PathVariable Long usuarioId) {
        return facturaService.obtenerFacturasDeUsuario(usuarioId);
    }

    @PutMapping("/{id}/pagar")
    public Factura pagarFactura(@PathVariable Long id) {
        return facturaService.pagarFactura(id);
    }

}
