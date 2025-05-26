package com.billetera.backend.infrastructure.Controller;


import com.billetera.backend.application.Services.TicketSoporteService;
import com.billetera.backend.domain.Entity.TicketSoporte;
import com.billetera.backend.infrastructure.dto.TicketRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketSoporteController {

    private final TicketSoporteService service;

    @PostMapping
    public ResponseEntity<TicketSoporte> crearTicket(
            @Valid @RequestBody TicketRequest request) {
        return ResponseEntity.ok(service.crearTicket(request));
    }
}