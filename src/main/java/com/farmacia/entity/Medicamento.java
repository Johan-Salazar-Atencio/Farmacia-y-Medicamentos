package com.farmacia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * RF-FAR-03/04/05/06: Medicamento del catálogo de farmacia.
 * El código único (RF-FAR-06) se genera en la capa de servicio, nunca lo asigna el cliente.
 * El stock NUNCA se edita manualmente desde el formulario: solo cambia al registrar un
 * lote (RF-FAR-11) para evitar inconsistencias con el inventario real.
 */
@Entity
@Table(name = "medicamentos")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"categoria", "lotes"})
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(length = 80)
    private String presentacion;

    @Column(length = 80)
    private String concentracion;

    @Column(name = "precio_venta", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVenta;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo = 10;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Estado estado = Estado.ACTIVO;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaMedicamento categoria;

    @OneToMany(mappedBy = "medicamento")
    private List<LoteMedicamento> lotes = new ArrayList<>();

    @PrePersist
    protected void alPersistir() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = Estado.ACTIVO;
        }
        if (this.stock == null) {
            this.stock = 0;
        }
    }
}
