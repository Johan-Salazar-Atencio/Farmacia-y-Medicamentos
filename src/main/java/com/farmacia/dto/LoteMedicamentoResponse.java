package com.farmacia.dto;

import com.farmacia.entity.Estado;
import com.farmacia.entity.LoteMedicamento;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO de salida (respuesta JSON) para LoteMedicamento. Aplana los datos de
 * medicamento y proveedor para no exponer las entidades ni sus proxies lazy.
 * Requiere que 'lote' venga con medicamento y proveedor ya cargados (JOIN FETCH),
 * como ya hace LoteMedicamentoRepository.
 */
public record LoteMedicamentoResponse(
        Long id,
        String numeroLote,
        Long medicamentoId,
        String medicamentoCodigo,
        String medicamentoNombre,
        Long proveedorId,
        String proveedorRazonSocial,
        Integer cantidad,
        LocalDate fechaVencimiento,
        LocalDate fechaIngreso,
        BigDecimal precioCompra,
        Estado estado
) {
    public static LoteMedicamentoResponse desde(LoteMedicamento lote) {
        return new LoteMedicamentoResponse(
                lote.getId(),
                lote.getNumeroLote(),
                lote.getMedicamento().getId(),
                lote.getMedicamento().getCodigo(),
                lote.getMedicamento().getNombre(),
                lote.getProveedor().getId(),
                lote.getProveedor().getRazonSocial(),
                lote.getCantidad(),
                lote.getFechaVencimiento(),
                lote.getFechaIngreso(),
                lote.getPrecioCompra(),
                lote.getEstado()
        );
    }
}
