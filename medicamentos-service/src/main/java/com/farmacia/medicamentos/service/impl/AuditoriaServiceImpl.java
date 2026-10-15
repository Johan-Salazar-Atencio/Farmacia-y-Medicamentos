package com.farmacia.medicamentos.service.impl;

import com.farmacia.medicamentos.audit.Operacion;
import com.farmacia.medicamentos.entity.AuditoriaLog;
import com.farmacia.medicamentos.repository.AuditoriaLogRepository;
import com.farmacia.medicamentos.service.AuditoriaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaLogRepository repository;

    public AuditoriaServiceImpl(AuditoriaLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public void registrar(String usuario, Operacion operacion, String entidad, Long registroId, String detalle) {
        AuditoriaLog log = new AuditoriaLog();
        log.setUsuario(usuario);
        log.setFechaHora(LocalDateTime.now());
        log.setOperacion(operacion);
        log.setEntidad(entidad);
        log.setRegistroId(registroId);
        log.setDetalle(detalle);
        repository.save(log);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditoriaLog> buscar(String entidad) {
        return repository.buscar(entidad == null || entidad.isBlank() ? null : entidad);
    }
}
