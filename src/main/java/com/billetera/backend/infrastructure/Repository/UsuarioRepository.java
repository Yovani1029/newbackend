package com.billetera.backend.infrastructure;

import com.billetera.backend.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByTelefono(String telefono);
    boolean existsByNumeroIdentificacion(String numeroIdentificacion);
    boolean existsByCorreo(String correo);
    boolean existsByTelefono(String telefono);


}
