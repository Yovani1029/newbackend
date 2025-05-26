package com.billetera.backend.application.Services;

import com.billetera.backend.domain.Entity.Agua;
import com.billetera.backend.domain.Entity.Cuenta;
import com.billetera.backend.domain.Entity.Factura;
import com.billetera.backend.domain.Entity.Internet;
import com.billetera.backend.domain.Entity.Luz;
import com.billetera.backend.domain.Entity.Transaccion;
import com.billetera.backend.domain.Entity.Usuario;
import com.billetera.backend.infrastructure.Repository.AguaRepository;
import com.billetera.backend.infrastructure.Repository.CuentaRepository;
import com.billetera.backend.infrastructure.Repository.FacturaRepository;
import com.billetera.backend.infrastructure.Repository.InternetRepository;
import com.billetera.backend.infrastructure.Repository.LuzRepository;
import com.billetera.backend.infrastructure.Repository.TransaccionRepository;
import com.billetera.backend.infrastructure.Repository.UsuarioRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class FacturaServiceImpl implements FacturaService {
    private final CuentaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;
    private final FacturaRepository facturaRepository;
    private final UsuarioRepository usuarioRepository;
    private final LuzRepository luzRepository;
    private final AguaRepository aguaRepository;
    private final InternetRepository internetRepository;

    public FacturaServiceImpl(
            FacturaRepository facturaRepository,
            UsuarioRepository usuarioRepository,
            LuzRepository luzRepository,
            AguaRepository aguaRepository,
            InternetRepository internetRepository,
            CuentaRepository cuentaRepository,
            TransaccionRepository transaccionRepository) {
        this.facturaRepository = facturaRepository;
        this.usuarioRepository = usuarioRepository;
        this.luzRepository = luzRepository;
        this.aguaRepository = aguaRepository;
        this.internetRepository = internetRepository;
        this.cuentaRepository = cuentaRepository;
        this.transaccionRepository = transaccionRepository;
    }

    @Override
    public Factura crearFactura(Factura factura, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (factura.getServicio() == null || factura.getServicio().isBlank()) {
            throw new RuntimeException("El campo 'servicio' es obligatorio");
        }

        if (factura.getMonto() == null) {
            factura.setMonto((double) Math.round(Math.random() * 100000.0));
        }

        if (factura.getEstado() == null) {
            factura.setEstado("pendiente");
        }

        if (factura.getFechaEmision() == null) {
            factura.setFechaEmision(LocalDate.now());
        }

        if (factura.getFechaLimite() == null) {
            factura.setFechaLimite(LocalDate.now().plusDays(15));
        }

        factura.setUsuario(usuario);

        factura = facturaRepository.save(factura);

        switch (factura.getServicio().toLowerCase()) {
            case "luz" -> {
                Luz luz = new Luz();
                luz.setNic(factura.getIdServicio());
                luz.setFactura(factura);
                luzRepository.save(luz);
            }
            case "agua" -> {
                Agua agua = new Agua();
                agua.setIdAgua(factura.getIdServicio());
                agua.setFactura(factura);
                aguaRepository.save(agua);
            }
            case "internet" -> {
                Internet internet = new Internet();
                internet.setIdInternet(factura.getIdServicio());
                internet.setFactura(factura);
                internetRepository.save(internet);
            }
            default -> throw new RuntimeException("Tipo de servicio no reconocido: " + factura.getServicio());
        }

        return factura;
    }

    @Override
    public Factura actualizarFactura(Long id, Factura factura) {
        Factura original = facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        original.setServicio(factura.getServicio());
        original.setIdServicio(factura.getIdServicio());
        original.setMonto(factura.getMonto());
        original.setEstado(factura.getEstado());
        original.setFechaEmision(factura.getFechaEmision());
        original.setFechaLimite(factura.getFechaLimite());

        return facturaRepository.save(original);
    }

    @Override
    public void eliminarFactura(Long id) {
        facturaRepository.deleteById(id);
    }

    @Override
    public List<Factura> obtenerFacturasDeUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getFacturas();
    }

    @Override
    public List<Factura> obtenerFacturasPendientes() {
        return facturaRepository.findByEstado("pendiente");
    }

    @Transactional
    public Factura pagarFactura(Long facturaId) {
        Factura factura = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));

        if (!"pendiente".equalsIgnoreCase(factura.getEstado())) {
            throw new RuntimeException("La factura ya fue pagada o no está pendiente");
        }

        Usuario usuario = factura.getUsuario();
        Cuenta cuenta = usuario.getCuenta();

        if (cuenta.getSaldo().compareTo(BigDecimal.valueOf(factura.getMonto())) < 0) {
            throw new RuntimeException("Saldo insuficiente para pagar la factura");
        }

        BigDecimal nuevoSaldo = cuenta.getSaldo().subtract(BigDecimal.valueOf(factura.getMonto()));
        cuenta.setSaldo(nuevoSaldo);

        factura.setEstado("pagada");

        Transaccion transaccion = new Transaccion();
        transaccion.setCuenta(cuenta);
        transaccion.setMonto(BigDecimal.valueOf(factura.getMonto()));
        transaccion.setTipoTransaccion("PAGO");
        transaccion.setDescripcion("Pago de factura de " + factura.getServicio());
        transaccion.setEstado("COMPLETADO");

        cuentaRepository.save(cuenta);
        facturaRepository.save(factura);
        transaccionRepository.save(transaccion);

        return factura;
    }

}
