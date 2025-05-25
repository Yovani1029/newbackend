package com.billetera.backend.infrastructure.dto;

import java.math.BigDecimal;

public class DepositoResponse {
    private boolean success;
    private String message;
    private String telefono;
    private BigDecimal monto;
    private BigDecimal nuevoSaldo;

    // Constructores
    public DepositoResponse() {}

    public DepositoResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public DepositoResponse(boolean success, String message, 
                          String telefono, BigDecimal monto, 
                          BigDecimal nuevoSaldo) {
        this.success = success;
        this.message = message;
        this.telefono = telefono;
        this.monto = monto;
        this.nuevoSaldo = nuevoSaldo;
    }

    // Getters y Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public BigDecimal getNuevoSaldo() {
        return nuevoSaldo;
    }

    public void setNuevoSaldo(BigDecimal nuevoSaldo) {
        this.nuevoSaldo = nuevoSaldo;
    }
}