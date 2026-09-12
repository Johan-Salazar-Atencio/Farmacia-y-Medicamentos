package com.farmacia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * RF-FAR-11/12/13: Lote físico de un medicamento, con fecha de vencimiento y proveedor
 * de origen. Cada lote registrado suma su cantidad al stock del medicamento (RF regla 3).
 */
@Entity
@Table(name = "lotes_medicamento")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"medicamento", "proveedor"})
public class LoteMedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_lote", nullable = false, length = 50)
    private String numeroLote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicamento_id", nullable = false)
    private Medicamento medicamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "fecha_ingreso", nullable = false, updatable = false)
    private LocalDate fechaIngreso;

    @Column(name = "precio_compra", precision = 10, scale = 2)
    private BigDecimal precioCompra;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Estado estado = Estado.ACTIVO;

    @PrePersist
    protected void alPersistir() {
        this.fechaIngreso = LocalDate.now();
        if (this.estado == null) {
            this.estado = Estado.ACTIVO;
        }
    }
}
