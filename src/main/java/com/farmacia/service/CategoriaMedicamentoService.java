package com.farmacia.service;

import com.farmacia.dto.CategoriaMedicamentoForm;
import com.farmacia.entity.CategoriaMedicamento;

import java.util.List;

public interface CategoriaMedicamentoService {

    List<CategoriaMedicamento> listarTodas();

    List<CategoriaMedicamento> listarActivas();

    CategoriaMedicamento obtenerPorId(Long id);

    CategoriaMedicamento crear(CategoriaMedicamentoForm form);

    CategoriaMedicamento actualizar(Long id, CategoriaMedicamentoForm form);

    void cambiarEstado(Long id);
}
