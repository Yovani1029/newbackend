package com.billetera.backend.application.Services;

import com.billetera.backend.domain.Entity.Cuenta;
import com.billetera.backend.domain.Entity.Usuario;
import com.billetera.backend.infrastructure.Repository.CuentaRepository;
import com.billetera.backend.infrastructure.Repository.TransaccionRepository;
import com.billetera.backend.infrastructure.Repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CuentaRepository cuentaRepository;
 private final TransaccionRepository transaccionRepository; 
    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, CuentaRepository cuentaRepository, TransaccionRepository transaccionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cuentaRepository = cuentaRepository;
        this.transaccionRepository = transaccionRepository;
    }

    @Override
    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        if (usuarioRepository.existsByTelefono(usuario.getTelefono())) {
            throw new IllegalArgumentException("El teléfono ya está registrado");
        }

        if (usuarioRepository.existsByNumeroIdentificacion(usuario.getNumeroIdentificacion())) {
            throw new IllegalArgumentException("El número de identificación ya está registrado");
        }

        if (usuario.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser en el futuro");
        }

        LocalDate fechaMinima = LocalDate.now().minusYears(15);
        if (usuario.getFechaNacimiento().isAfter(fechaMinima)) {
            throw new IllegalArgumentException("Debes tener al menos 15 años para registrarte");
        }

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Cuenta cuenta = new Cuenta();
        cuenta.setSaldo(BigDecimal.ZERO);
        cuenta.setUsuario(usuarioGuardado);
        cuentaRepository.save(cuenta);

        usuarioGuardado.setCuenta(cuenta);
        return usuarioGuardado;
    }

    @Override
    public Usuario login(String telefono, String contrasena) {
        return usuarioRepository.findByTelefono(telefono)
            .filter(usuario -> usuario.getContrasena().equals(contrasena))
            .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));
    }

    @Override
    public BigDecimal consultarSaldo(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario != null && usuario.getCuenta() != null) {
            return usuario.getCuenta().getSaldo();
        }
        return null;
    }

    @Override
public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
    Usuario usuarioExistente = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

    usuarioExistente.setNombre(usuarioActualizado.getNombre());
    usuarioExistente.setApellido(usuarioActualizado.getApellido());
    usuarioExistente.setCorreo(usuarioActualizado.getCorreo());
    usuarioExistente.setTelefono(usuarioActualizado.getTelefono());

    // Solo actualiza contraseña si no es nula ni vacía
    if (usuarioActualizado.getContrasena() != null && !usuarioActualizado.getContrasena().isBlank()) {
        usuarioExistente.setContrasena(usuarioActualizado.getContrasena());
    }

    return usuarioRepository.save(usuarioExistente);
}


        @Override
@Transactional
public void eliminarUsuario(Long id) {
    Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

    Long cuentaId = usuario.getCuenta() != null ? usuario.getCuenta().getId() : null;

    if (cuentaId != null) {
        // Eliminar transacciones u otras entidades relacionadas si es necesario
        transaccionRepository.deleteByCuentaId(cuentaId); 

        cuentaRepository.deleteById(cuentaId);
    }

    usuarioRepository.deleteById(id);
}

    

}
