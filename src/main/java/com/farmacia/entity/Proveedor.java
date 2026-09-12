package com.farmacia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * RF-FAR-10: Proveedor de medicamentos.
 * Relación: Proveedor 1 -> N LoteMedicamento.
 */
@Entity
@Table(name = "proveedores")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "lotes")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String ruc;

    @Column(name = "razon_social", nullable = false, length = 150)
    private String razonSocial;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String email;

    @Column(length = 200)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Estado estado = Estado.ACTIVO;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @OneToMany(mappedBy = "proveedor")
    private List<LoteMedicamento> lotes = new ArrayList<>();

    @PrePersist
    protected void alPersistir() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = Estado.ACTIVO;
        }
    }
}
