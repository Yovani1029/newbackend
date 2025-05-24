package com.billetera.backend.domain.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "luz")
public class Luz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nic;

    @OneToOne
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;
}
