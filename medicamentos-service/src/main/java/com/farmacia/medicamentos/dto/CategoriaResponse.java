package com.farmacia.medicamentos.dto;

import com.farmacia.medicamentos.entity.CategoriaMedicamento;
import com.farmacia.medicamentos.entity.Estado;

public record CategoriaResponse(Long id, String nombre, String descripcion, Estado estado) {
    public static CategoriaResponse desde(CategoriaMedicamento c) {
        return new CategoriaResponse(c.getId(), c.getNombre(), c.getDescripcion(), c.getEstado());
    }
}
