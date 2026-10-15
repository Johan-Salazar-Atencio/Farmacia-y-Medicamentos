package com.farmacia.service;

import com.farmacia.dto.MedicamentoForm;
import com.farmacia.entity.Estado;
import com.farmacia.entity.Medicamento;

import java.util.List;

public interface MedicamentoService {

    List<Medicamento> buscar(Estado estado, Long categoriaId);

    Medicamento obtenerPorId(Long id);

    Medicamento crear(MedicamentoForm form);

    Medicamento actualizar(Long id, MedicamentoForm form);

    void cambiarEstado(Long id);
}
