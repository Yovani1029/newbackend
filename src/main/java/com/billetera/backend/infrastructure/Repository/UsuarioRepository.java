package com.billetera.backend.infrastructure.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billetera.backend.domain.Entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByTelefono(String telefono);
    boolean existsByNumeroIdentificacion(String numeroIdentificacion);
    boolean existsByCorreo(String correo);
    boolean existsByTelefono(String telefono);


}
