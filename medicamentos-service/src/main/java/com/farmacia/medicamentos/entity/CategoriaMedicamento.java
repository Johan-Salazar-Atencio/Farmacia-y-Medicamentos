package com.farmacia.medicamentos.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/** Lado "uno" de la relación Categoría 1 --- N Medicamento. */
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

    @OneToMany(mappedBy = "categoria")
    private List<Medicamento> medicamentos = new ArrayList<>();
}
