package com.billetera.backend.infrastructure.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class DepositoRequest {
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{10}$", message = "Teléfono debe tener 10 dígitos")
    private String telefono;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor que cero")
    private BigDecimal monto;

    public DepositoRequest() {}

    public DepositoRequest(String telefono, BigDecimal monto) {
        this.telefono = telefono;
        this.monto = monto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}