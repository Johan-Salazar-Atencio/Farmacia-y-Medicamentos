package com.farmacia.medicamentos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Lado "muchos" de Medicamento 1 --- N Lote. La FK medicamento_id es obligatoria
 * (no puede existir un lote huérfano). proveedor_id es una referencia lógica al
 * microservicio catalogo-proveedores (otro servicio = otra BD, por eso no es @ManyToOne).
 */
@Entity
@Table(name = "lotes_medicamento",
        uniqueConstraints = @UniqueConstraint(columnNames = {"medicamento_id", "numero_lote"}))
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "medicamento")
public class LoteMedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_lote", nullable = false, length = 50)
    private String numeroLote;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "proveedor_id")
    private Long proveedorId;

    @Column(name = "fecha_ingreso", nullable = false, updatable = false)
    private LocalDateTime fechaIngreso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicamento_id", nullable = false)
    private Medicamento medicamento;

    @PrePersist
    protected void alPersistir() {
        this.fechaIngreso = LocalDateTime.now();
    }
}
