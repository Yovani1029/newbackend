package com.billetera.backend.application.Services;

import java.util.List;

import com.billetera.backend.domain.Entity.Factura;

public interface FacturaService {
    Factura crearFactura(Factura factura, Long usuarioId);
    Factura actualizarFactura(Long id, Factura factura);
    void eliminarFactura(Long id);
    List<Factura> obtenerFacturasDeUsuario(Long usuarioId);
      List<Factura> obtenerFacturasPendientes();
      
}
