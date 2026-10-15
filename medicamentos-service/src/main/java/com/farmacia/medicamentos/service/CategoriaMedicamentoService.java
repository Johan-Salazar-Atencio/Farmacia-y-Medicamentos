package com.farmacia.medicamentos.service;

import com.farmacia.medicamentos.dto.CategoriaForm;
import com.farmacia.medicamentos.entity.CategoriaMedicamento;

import java.util.List;

public interface CategoriaMedicamentoService {
    List<CategoriaMedicamento> listarTodas();
    List<CategoriaMedicamento> listarActivas();
    CategoriaMedicamento obtenerPorId(Long id);
    CategoriaMedicamento crear(CategoriaForm form);
    CategoriaMedicamento actualizar(Long id, CategoriaForm form);
    void cambiarEstado(Long id);
}
