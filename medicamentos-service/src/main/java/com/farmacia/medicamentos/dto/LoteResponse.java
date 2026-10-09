package com.farmacia.medicamentos.dto;

import com.farmacia.medicamentos.entity.LoteMedicamento;

import java.time.LocalDate;

public record LoteResponse(Long id, Long medicamentoId, String medicamentoNombre, String numeroLote,
                           LocalDate fechaVencimiento, Integer cantidad, Long proveedorId) {
    /** Requiere el medicamento ya cargado (JOIN FETCH o recién asignado). */
    public static LoteResponse desde(LoteMedicamento l) {
        return new LoteResponse(l.getId(), l.getMedicamento().getId(), l.getMedicamento().getNombre(),
                l.getNumeroLote(), l.getFechaVencimiento(), l.getCantidad(), l.getProveedorId());
    }
}
