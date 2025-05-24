package com.billetera.backend.infrastructure;
import java.util.List;
import com.billetera.backend.domain.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findByEstado(String estado);

}
