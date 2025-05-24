package com.billetera.backend.infrastructure;

import com.billetera.backend.domain.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    
    @Query("SELECT c FROM Cuenta c JOIN c.usuario u WHERE u.telefono = :telefono")
    Optional<Cuenta> findByUsuarioTelefono(@Param("telefono") String telefono);
    
    @Query("SELECT c FROM Cuenta c WHERE c.usuario.id = :usuarioId")
    Optional<Cuenta> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}