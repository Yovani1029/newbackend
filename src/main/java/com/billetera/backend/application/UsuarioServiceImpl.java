package com.billetera.backend.application;

import com.billetera.backend.domain.Usuario;
import com.billetera.backend.domain.Cuenta;
import com.billetera.backend.infrastructure.UsuarioRepository;
import com.billetera.backend.infrastructure.CuentaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CuentaRepository cuentaRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, CuentaRepository cuentaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.cuentaRepository = cuentaRepository;
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
        Usuario usuario = usuarioRepository.findByTelefono(telefono);
        if (usuario != null && usuario.getContrasena().equals(contrasena)) {
            return usuario;
        }
        return null;
    }

    @Override
    public BigDecimal consultarSaldo(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario != null && usuario.getCuenta() != null) {
            return usuario.getCuenta().getSaldo();
        }
        return null;
    }

}
