package com.farmacia.dto;

import com.farmacia.entity.CategoriaMedicamento;
import com.farmacia.entity.Estado;

import java.time.LocalDateTime;

/**
 * DTO de salida (respuesta JSON) para CategoriaMedicamento. No expone la lista
 * 'medicamentos' (lazy) para evitar LazyInitializationException y payloads gigantes.
 */
public record CategoriaMedicamentoResponse(
        Long id,
        String nombre,
        String descripcion,
        Estado estado,
        LocalDateTime fechaCreacion
) {
    public static CategoriaMedicamentoResponse desde(CategoriaMedicamento categoria) {
        return new CategoriaMedicamentoResponse(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getEstado(),
                categoria.getFechaCreacion()
        );
    }
}
