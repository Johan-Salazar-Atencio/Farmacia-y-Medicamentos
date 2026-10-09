
package com.farmacia.core.dto;

import com.farmacia.core.entity.Auditoria;

import java.time.LocalDateTime;

public record AuditoriaResponse(
        Long id,
        LocalDateTime fechaHora,
        String usuario,
        String accion,
        String entidad,
        Long idEntidad,
        String detalle,
        String ipOrigen,
        String userAgent
) {

    public static AuditoriaResponse desde(Auditoria auditoria) {
        return new AuditoriaResponse(
                auditoria.getId(),
                auditoria.getFechaHora(),
                auditoria.getUsuario(),
                auditoria.getAccion().name(),
                auditoria.getEntidad(),
                auditoria.getIdEntidad(),
                auditoria.getDetalle(),
                auditoria.getIpOrigen(),
                auditoria.getUserAgent()
        );
    }
}
