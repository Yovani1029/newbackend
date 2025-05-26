package com.billetera.backend.domain.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "correo" }),
        @UniqueConstraint(columnNames = { "telefono" }),
        @UniqueConstraint(columnNames = { "numeroIdentificacion" })
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    @Column(nullable = false, length = 20)
    private String tipoIdentificacion;

    @Column(nullable = false, length = 30)
    private String numeroIdentificacion;

    @Column(nullable = false, length = 10)
    @Pattern (regexp = "\\d{10}", message = "El teléfono debe tener exactamente 10 dígitos numéricos")
    private String telefono;

    @Column(nullable = false, length = 100)
    private String correo;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false, length = 255)
    @Pattern(regexp = "\\d{4}", message = "La contraseña debe ser numérica y de exactamente 4 dígitos")
    private String contrasena;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Cuenta cuenta;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
@JsonManagedReference
private List<Factura> facturas;

 public String getEmail() {
        return this.correo;
    }
    
    public void setEmail(String email) {
        this.correo = email;
    }
}
