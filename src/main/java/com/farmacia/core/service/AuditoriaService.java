package com.farmacia.core.service;

import com.farmacia.core.entity.AccionAuditoria;
import com.farmacia.core.entity.Auditoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditoriaService {

    void registrar(Auditoria auditoria);

    void registrar(AccionAuditoria accion, String usuario, String entidad,
                   Long idEntidad, String detalle, String ip, String userAgent);

    Page<Auditoria> buscar(LocalDateTime desde, LocalDateTime hasta,
                           String usuario, AccionAuditoria accion, Pageable pageable);

    List<Auditoria> listarRecientes(int limite);
}