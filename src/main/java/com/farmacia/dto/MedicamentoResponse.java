package com.farmacia.dto;

import com.farmacia.entity.Estado;
import com.farmacia.entity.Medicamento;

import java.math.BigDecimal;

/**
 * DTO de salida (respuesta JSON) para Medicamento. Es un "record" plano: nunca expone
 * la entidad ni sus relaciones lazy (categoria, lotes) directamente, evitando tanto
 * LazyInitializationException como referencias circulares al serializar con Jackson.
 */
public record MedicamentoResponse(
        Long id,
        String codigo,
        String nombre,
        String descripcion,
        String presentacion,
        String concentracion,
        BigDecimal precioVenta,
        Integer stock,
        Integer stockMinimo,
        Estado estado,
        Long categoriaId,
        String categoriaNombre
) {

    /**
     * Requiere que 'medicamento' venga con la categoría ya cargada (JOIN FETCH),
     * como ya hace MedicamentoRepository.buscar(...).
     */
    public static MedicamentoResponse desde(Medicamento medicamento) {
        return new MedicamentoResponse(
                medicamento.getId(),
                medicamento.getCodigo(),
                medicamento.getNombre(),
                medicamento.getDescripcion(),
                medicamento.getPresentacion(),
                medicamento.getConcentracion(),
                medicamento.getPrecioVenta(),
                medicamento.getStock(),
                medicamento.getStockMinimo(),
                medicamento.getEstado(),
                medicamento.getCategoria().getId(),
                medicamento.getCategoria().getNombre()
        );
    }
}
