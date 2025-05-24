package com.billetera.backend.domain.Entity;

import lombok.*;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal saldo = BigDecimal.ZERO;
    
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    @JsonBackReference
    private Usuario usuario;
}
