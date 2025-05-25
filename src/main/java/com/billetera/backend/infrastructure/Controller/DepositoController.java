package com.billetera.backend.infrastructure.Controller;

import com.billetera.backend.application.Services.DepositoService;
import com.billetera.backend.infrastructure.dto.DepositoRequest;
import com.billetera.backend.infrastructure.dto.DepositoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/depositos")
@CrossOrigin(origins = "http://localhost:8100")
public class DepositoController {
    private final DepositoService depositoService;

    public DepositoController(DepositoService depositoService) {
        this.depositoService = depositoService;
    }

    @PostMapping
    public ResponseEntity<DepositoResponse> depositar(
        @RequestBody DepositoRequest request
    ) {
        DepositoResponse response = depositoService.realizarDeposito(
            request.getTelefono(),
            request.getMonto()
        );
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<DepositoResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(
            new DepositoResponse(false, ex.getMessage())
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<DepositoResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(404).body(
            new DepositoResponse(false, ex.getMessage())
        );
    }
}