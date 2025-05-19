package com.billetera.backend.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TransferenciaRequest {

    @NotBlank(message = "El teléfono remitente es obligatorio")
    @Pattern(regexp = "^[0-9]{10}$", message = "Teléfono debe tener 10 dígitos")
    private String telefonoRemitente;

    @NotBlank(message = "El teléfono destinatario es obligatorio")
    @Pattern(regexp = "^[0-9]{10}$", message = "Teléfono debe tener 10 dígitos")
    private String telefonoDestinatario;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor que cero")
    private BigDecimal monto;

    public String getTelefonoRemitente() {
        return telefonoRemitente;
    }

    public void setTelefonoRemitente(String telefonoRemitente) {
        this.telefonoRemitente = telefonoRemitente;
    }

    public String getTelefonoDestinatario() {
        return telefonoDestinatario;
    }

    public void setTelefonoDestinatario(String telefonoDestinatario) {
        this.telefonoDestinatario = telefonoDestinatario;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}
