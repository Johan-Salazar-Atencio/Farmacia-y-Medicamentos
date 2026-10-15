
package com.farmacia.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"password", "roles"})
public class Usuario {

    public static final int MAX_INTENTOS = 5;
    public static final int MINUTOS_BLOQUEO = 15;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "intentos_fallidos", nullable = false)
    private Integer intentosFallidos = 0;

    @Column(name = "bloqueado_hasta")
    private LocalDateTime bloqueadoHasta;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuarios_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new LinkedHashSet<>();

    @PrePersist
    protected void alPersistir() {
        this.fechaRegistro = LocalDateTime.now();

        if (this.activo == null) {
            this.activo = true;
        }

        if (this.intentosFallidos == null) {
            this.intentosFallidos = 0;
        }
    }

    public boolean estaBloqueado() {
        return bloqueadoHasta != null
                && bloqueadoHasta.isAfter(LocalDateTime.now());
    }

    public boolean estaActivo() {
        return Boolean.TRUE.equals(activo);
    }
}
