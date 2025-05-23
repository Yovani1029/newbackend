package com.billetera.backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "agua")
public class Agua {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idAgua;

    @OneToOne
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;
}
