package com.farmacia.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditorias", indexes = {
        @Index(name = "idx_auditoria_fecha", columnList = "fecha_hora"),
        @Index(name = "idx_auditoria_usuario", columnList = "usuario"),
        @Index(name = "idx_auditoria_accion", columnList = "accion")
})
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false, length = 50)
    private String usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccionAuditoria accion;

    @Column(length = 60)
    private String entidad;

    private Long idEntidad;

    @Column(length = 1000)
    private String detalle;

    @Column(name = "ip_origen", length = 60)
    private String ipOrigen;

    @Column(name = "user_agent", length = 255)
    private String userAgent;

    public static Auditoria de(
            AccionAuditoria accion,
            String usuario,
            String entidad) {

        Auditoria registro = new Auditoria();
        registro.fechaHora = LocalDateTime.now();
        registro.usuario = usuario == null || usuario.isBlank()
                ? "anónimo"
                : usuario;
        registro.accion = accion;
        registro.entidad = entidad;

        return registro;
    }
}