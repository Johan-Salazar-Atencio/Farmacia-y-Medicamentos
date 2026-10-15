package com.farmacia.medicamentos.dto;

import com.farmacia.medicamentos.audit.Operacion;
import com.farmacia.medicamentos.entity.AuditoriaLog;

import java.time.LocalDateTime;

public record AuditoriaResponse(Long id, String usuario, LocalDateTime fechaHora, Operacion operacion,
                                String entidad, Long registroId, String detalle) {
    public static AuditoriaResponse desde(AuditoriaLog a) {
        return new AuditoriaResponse(a.getId(), a.getUsuario(), a.getFechaHora(), a.getOperacion(),
                a.getEntidad(), a.getRegistroId(), a.getDetalle());
    }
}
