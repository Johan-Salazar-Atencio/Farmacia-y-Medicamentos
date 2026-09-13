package com.farmacia.dto;

import com.farmacia.entity.Estado;
import com.farmacia.entity.Medicamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MedicamentoResponse(
        Long id, String codigo, String nombre, String descripcion,
        BigDecimal precioVenta, Integer stock,
        Long categoriaId, String categoriaNombre,
        Estado estado, LocalDateTime fechaRegistro
) {
    public static MedicamentoResponse desde(Medicamento m) {
        return new MedicamentoResponse(
                m.getId(), m.getCodigo(), m.getNombre(), m.getDescripcion(),
                m.getPrecioVenta(), m.getStock(),
                m.getCategoria() != null ? m.getCategoria().getId() : null,
                m.getCategoria() != null ? m.getCategoria().getNombre() : null,
                m.getEstado(), m.getFechaRegistro()
        );
    }
}