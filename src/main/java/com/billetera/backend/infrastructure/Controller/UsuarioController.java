package com.billetera.backend.infrastructure.Controller;

import com.billetera.backend.application.Services.UsuarioService;
import com.billetera.backend.domain.Entity.Usuario;

import jakarta.validation.Valid;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:8100")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public Usuario registrar(@Valid @RequestBody Usuario usuario) {
        return usuarioService.registrar(usuario);
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario usuario) {
        return usuarioService.login(usuario.getTelefono(), usuario.getContrasena());
    }

    @GetMapping("/{id}/saldo")
    public BigDecimal saldo(@PathVariable Long id) {
        return usuarioService.consultarSaldo(id);
    }
     @PutMapping("/{id}")
    public Usuario actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        return usuarioService.actualizarUsuario(id, usuarioActualizado);
}
    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
    usuarioService.eliminarUsuario(id);
}
}
