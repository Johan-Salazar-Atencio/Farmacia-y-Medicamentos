package com.farmacia.medicamentos.config;

import com.farmacia.medicamentos.audit.UsuarioActual;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AppConfig {

    /** Alimenta @CreatedBy / @LastModifiedBy de Medicamento con el usuario actual. */
    @Bean
    public AuditorAware<String> auditorAware(UsuarioActual usuarioActual) {
        return () -> Optional.of(usuarioActual.obtener());
    }
}
