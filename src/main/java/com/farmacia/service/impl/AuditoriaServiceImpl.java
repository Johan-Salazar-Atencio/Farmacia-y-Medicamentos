package com.farmacia.core.service.impl;

import com.farmacia.core.entity.AccionAuditoria;
import com.farmacia.core.entity.Auditoria;
import com.farmacia.core.repository.AuditoriaRepository;
import com.farmacia.core.service.AuditoriaService;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AuditoriaServiceImpl implements AuditoriaService {

    private static final Logger log =
            LoggerFactory.getLogger(AuditoriaServiceImpl.class);

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaServiceImpl(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(Auditoria auditoria) {
        try {
            auditoriaRepository.save(auditoria);
        } catch (Exception ex) {
            log.warn("No se pudo registrar la auditoría {}: {}",
                    auditoria.getAccion(), ex.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(AccionAuditoria accion, String usuario,
                          String entidad, Long idEntidad, String detalle,
                          String ip, String userAgent) {
        try {
            Auditoria registro = Auditoria.de(accion, usuario, entidad);
            registro.setIdEntidad(idEntidad);
            registro.setDetalle(detalle == null ? null : truncate(detalle, 1000));
            registro.setIpOrigen(ip);
            registro.setUserAgent(
                    userAgent == null ? null : truncate(userAgent, 255));
            auditoriaRepository.save(registro);
        } catch (Exception ex) {
            log.warn("No se pudo registrar la auditoría {}: {}",
                    accion, ex.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public Page<Auditoria> buscar(LocalDateTime desde, LocalDateTime hasta,
                                  String usuario, AccionAuditoria accion,
                                  Pageable pageable) {
        return auditoriaRepository.findAll(
                construirSpec(desde, hasta, usuario, accion), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Auditoria> listarRecientes(int limite) {
        Page<Auditoria> pagina =
                auditoriaRepository.findAllByOrderByFechaHoraDesc(
                        org.springframework.data.domain.PageRequest.of(0, limite));
        return pagina.getContent();
    }

    private Specification<Auditoria> construirSpec(
            LocalDateTime desde, LocalDateTime hasta,
            String usuario, AccionAuditoria accion) {

        return (root, query, cb) -> {
            List<Predicate> predicados = new ArrayList<>();

            if (desde != null) {
                predicados.add(
                        cb.greaterThanOrEqualTo(root.get("fechaHora"), desde));
            }

            if (hasta != null) {
                predicados.add(
                        cb.lessThanOrEqualTo(root.get("fechaHora"), hasta));
            }

            if (usuario != null && !usuario.isBlank()) {
                predicados.add(cb.like(
                        cb.lower(root.get("usuario")),
                        "%" + usuario.trim().toLowerCase() + "%"));
            }

            if (accion != null) {
                predicados.add(cb.equal(root.get("accion"), accion));
            }

            return cb.and(predicados.toArray(new Predicate[0]));
        };
    }

    private String truncate(String valor, int max) {
        return valor.length() <= max ? valor : valor.substring(0, max);
    }
}