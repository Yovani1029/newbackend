package com.billetera.backend.infrastructure.dto;

import java.math.BigDecimal;

public record RetiroResponse(
        boolean exito,
        String mensaje,
        String telefono,
        BigDecimal montoRetirado,
        BigDecimal nuevoSaldo
) {}