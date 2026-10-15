package com.farmacia.dto;

import com.farmacia.entity.Estado;
import com.farmacia.entity.Proveedor;

import java.time.LocalDateTime;

/**
 * DTO de salida (respuesta JSON) para Proveedor. No expone la lista 'lotes' (lazy)
 * para evitar LazyInitializationException y payloads gigantes.
 */
public record ProveedorResponse(
        Long id,
        String ruc,
        String razonSocial,
        String telefono,
        String email,
        String direccion,
        Estado estado,
        LocalDateTime fechaRegistro
) {
    public static ProveedorResponse desde(Proveedor proveedor) {
        return new ProveedorResponse(
                proveedor.getId(),
                proveedor.getRuc(),
                proveedor.getRazonSocial(),
                proveedor.getTelefono(),
                proveedor.getEmail(),
                proveedor.getDireccion(),
                proveedor.getEstado(),
                proveedor.getFechaRegistro()
        );
    }
}
