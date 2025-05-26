package com.billetera.backend.infrastructure.Controller;

import com.billetera.backend.application.Services.RetiroService;
import com.billetera.backend.infrastructure.dto.RetiroResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/retiros")
@RequiredArgsConstructor
public class RetiroController {

    private final RetiroService retiroService;

    @PostMapping("/solicitar-codigo")
    public String solicitarCodigo(@RequestParam String telefono) {
        retiroService.solicitarCodigoRetiro(telefono);
        return "Se ha enviado un código de verificación a tu correo electrónico registrado";
    }

    @PostMapping("/realizar")
    public RetiroResponse realizarRetiro(
            @RequestParam String telefono,
            @RequestParam BigDecimal monto,
            @RequestParam String codigo) {
        return retiroService.realizarRetiro(telefono, monto, codigo);
    }
}