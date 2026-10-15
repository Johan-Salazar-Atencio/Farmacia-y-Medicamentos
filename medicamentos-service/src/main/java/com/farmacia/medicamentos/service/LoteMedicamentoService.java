package com.farmacia.medicamentos.service;

import com.farmacia.medicamentos.dto.LoteForm;
import com.farmacia.medicamentos.entity.LoteMedicamento;

import java.util.List;

public interface LoteMedicamentoService {
    List<LoteMedicamento> buscar(Long medicamentoId);
    LoteMedicamento obtenerPorId(Long id);
    LoteMedicamento crear(LoteForm form);
    LoteMedicamento actualizar(Long id, LoteForm form);
    void eliminar(Long id);
}
