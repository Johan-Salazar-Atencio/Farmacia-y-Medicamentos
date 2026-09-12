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
 * RF-FAR-08: Categoría a la que pertenece un medicamento (ej. Analgésicos, Antibióticos).
 * Relación: CategoriaMedicamento 1 -> N Medicamento.
 */
@Entity
@Table(name = "categorias_medicamento")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "medicamentos")
public class CategoriaMedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Estado estado = Estado.ACTIVO;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "categoria")
    private List<Medicamento> medicamentos = new ArrayList<>();

    @PrePersist
    protected void alPersistir() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = Estado.ACTIVO;
        }
    }
}
