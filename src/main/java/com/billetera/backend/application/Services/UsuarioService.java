package com.billetera.backend.application;

import java.math.BigDecimal;

import com.billetera.backend.domain.Usuario;

public interface UsuarioService {

    Usuario registrar(Usuario usuario);

    Usuario login(String telefono, String contrasena);

    BigDecimal consultarSaldo(Long usuarioId);
    Usuario actualizarUsuario(Long id, Usuario usuarioActualizado);

    void eliminarUsuario(Long id);

}
