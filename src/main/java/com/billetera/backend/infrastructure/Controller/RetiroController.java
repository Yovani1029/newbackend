package com.billetera.backend.infrastructure.Controller;

import com.billetera.backend.application.Services.RetiroService;
import com.billetera.backend.infrastructure.dto.RetiroRequest;
import com.billetera.backend.infrastructure.dto.RetiroResponse;
import com.billetera.backend.infrastructure.dto.TelefonoRequest;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/retiros")
@RequiredArgsConstructor
public class RetiroController {

    private final RetiroService retiroService;

    @PostMapping("/solicitar-codigo")
    public Map<String, String> solicitarCodigo(@RequestBody TelefonoRequest request) {
        retiroService.solicitarCodigoRetiro(request.getTelefono());
        return Map.of("mensaje", "Se ha enviado un código de verificación a tu correo electrónico registrado");
    }

    @PostMapping("/realizar")
    public RetiroResponse realizarRetiro(@RequestBody RetiroRequest request) {
        return retiroService.realizarRetiro(
                request.getTelefono(),
                request.getMonto(),
                request.getCodigo());
    }
}
