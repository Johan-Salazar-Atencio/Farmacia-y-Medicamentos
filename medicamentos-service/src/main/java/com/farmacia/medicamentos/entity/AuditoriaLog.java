package com.farmacia.medicamentos.entity;

import com.farmacia.medicamentos.audit.Operacion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** Bitácora de auditoría (Pregunta 2). Una fila por operación crítica. */
@Entity
@Table(name = "auditoria")
@Getter
@Setter
@NoArgsConstructor
public class AuditoriaLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String usuario;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Operacion operacion;

    @Column(nullable = false, length = 50)
    private String entidad;

    @Column(name = "registro_id")
    private Long registroId;

    @Column(length = 255)
    private String detalle;
}
