package com.farmacia.medicamentos.service;

import com.farmacia.medicamentos.dto.MedicamentoForm;
import com.farmacia.medicamentos.entity.Estado;
import com.farmacia.medicamentos.entity.Medicamento;

import java.util.List;

public interface MedicamentoService {

    List<Medicamento> buscar(Estado estado, Long categoriaId);

    Medicamento obtenerPorId(Long id);

    Medicamento crear(MedicamentoForm form);

    Medicamento actualizar(Long id, MedicamentoForm form);

    void cambiarEstado(Long id);
}
