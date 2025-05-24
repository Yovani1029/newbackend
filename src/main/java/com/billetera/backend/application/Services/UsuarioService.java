package com.billetera.backend.application.Services;

import java.math.BigDecimal;

import com.billetera.backend.domain.Entity.Usuario;

public interface UsuarioService {

    Usuario registrar(Usuario usuario);

    Usuario login(String telefono, String contrasena);

    BigDecimal consultarSaldo(Long usuarioId);
    Usuario actualizarUsuario(Long id, Usuario usuarioActualizado);

    void eliminarUsuario(Long id);

}
