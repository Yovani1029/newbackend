package com.billetera.backend.application.Services;

import com.billetera.backend.domain.Entity.Agua;
import com.billetera.backend.domain.Entity.Factura;
import com.billetera.backend.domain.Entity.Internet;
import com.billetera.backend.domain.Entity.Luz;
import com.billetera.backend.domain.Entity.Usuario;
import com.billetera.backend.infrastructure.Repository.AguaRepository;
import com.billetera.backend.infrastructure.Repository.FacturaRepository;
import com.billetera.backend.infrastructure.Repository.InternetRepository;
import com.billetera.backend.infrastructure.Repository.LuzRepository;
import com.billetera.backend.infrastructure.Repository.UsuarioRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FacturaServiceImpl implements FacturaService {

    private final FacturaRepository facturaRepository;
    private final UsuarioRepository usuarioRepository;
    private final LuzRepository luzRepository;
    private final AguaRepository aguaRepository;
    private final InternetRepository internetRepository;

    public FacturaServiceImpl(FacturaRepository facturaRepository,
                              UsuarioRepository usuarioRepository,
                              LuzRepository luzRepository,
                              AguaRepository aguaRepository,
                              InternetRepository internetRepository) {
        this.facturaRepository = facturaRepository;
        this.usuarioRepository = usuarioRepository;
        this.luzRepository = luzRepository;
        this.aguaRepository = aguaRepository;
        this.internetRepository = internetRepository;
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

        // Guardamos la factura principal
        factura = facturaRepository.save(factura);

        // Guardamos la entidad específica
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
}
