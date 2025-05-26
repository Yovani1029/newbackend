package com.billetera.backend.infrastructure.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RetiroRequest {
    private String telefono;
    private BigDecimal monto;
    private String codigo;
}
