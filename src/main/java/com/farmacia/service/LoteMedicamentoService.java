package com.farmacia.service;

import com.farmacia.dto.LoteMedicamentoForm;
import com.farmacia.entity.LoteMedicamento;

import java.util.List;

public interface LoteMedicamentoService {

    LoteMedicamento registrar(LoteMedicamentoForm form);

    LoteMedicamento obtenerPorId(Long id);

    List<LoteMedicamento> listarActivosVigentes();

    List<LoteMedicamento> listarVencidos();

    /**
     * Lotes activos cuyo vencimiento cae entre hoy+diasDesde y hoy+diasHasta (ambos inclusive).
     */
    List<LoteMedicamento> listarPorVencerEnRango(int diasDesde, int diasHasta);

    void anular(Long id);
}