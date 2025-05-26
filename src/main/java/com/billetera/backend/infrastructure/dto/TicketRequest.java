package com.billetera.backend.infrastructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record TicketRequest(
    @NotBlank String asunto,
    @NotBlank String mensaje,
    @NotBlank String nombreContacto,
    @Email @NotBlank String correoContacto,
    Long usuarioId 
) {}