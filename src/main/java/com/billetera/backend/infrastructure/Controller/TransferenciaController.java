package com.billetera.backend.infrastructure;

import com.billetera.backend.application.TransferenciaService;
import com.billetera.backend.infrastructure.dto.TransferenciaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transferencias")
@CrossOrigin(origins = "http://localhost:8100")
public class TransferenciaController {

    @Autowired
    private TransferenciaService transferenciaService;

@PostMapping("/enviar")
public ResponseEntity<?> realizarTransferencia(@RequestBody TransferenciaRequest request) {
    System.out.println("📩 Teléfono remitente: " + request.getTelefonoRemitente());
    System.out.println("📩 Teléfono destinatario: " + request.getTelefonoDestinatario());
    System.out.println("💰 Monto: " + request.getMonto());

    try {
        String resultado = transferenciaService.transferirPorTelefono(
            request.getTelefonoRemitente(),
            request.getTelefonoDestinatario(),
            request.getMonto()
        );
        return ResponseEntity.ok().body(resultado);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.internalServerError().body("Error procesando la transferencia");
    }
}

}