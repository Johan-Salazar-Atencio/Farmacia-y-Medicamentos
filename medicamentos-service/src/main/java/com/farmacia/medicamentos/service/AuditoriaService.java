package com.farmacia.medicamentos.service;

import com.farmacia.medicamentos.audit.Operacion;
import com.farmacia.medicamentos.entity.AuditoriaLog;

import java.util.List;

public interface AuditoriaService {
    void registrar(String usuario, Operacion operacion, String entidad, Long registroId, String detalle);
    List<AuditoriaLog> buscar(String entidad);
}
