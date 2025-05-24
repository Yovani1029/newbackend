package com.billetera.backend.domain.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "internet")
public class Internet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idInternet;

    @OneToOne
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;
}
