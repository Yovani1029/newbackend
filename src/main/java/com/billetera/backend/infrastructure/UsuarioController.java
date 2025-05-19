package com.billetera.backend.infrastructure;

import com.billetera.backend.domain.Usuario;
import com.billetera.backend.application.UsuarioService;
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
}
