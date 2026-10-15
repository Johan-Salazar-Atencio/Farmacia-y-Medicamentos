
package com.farmacia.core.controller;

import com.farmacia.core.dto.AuditoriaResponse;
import com.farmacia.core.entity.AccionAuditoria;
import com.farmacia.core.service.AuditoriaService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/auditorias")
@PreAuthorize("hasRole('ADMIN')")
public class AuditoriaRestController {

    private final AuditoriaService auditoriaService;

    public AuditoriaRestController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    public List<AuditoriaResponse> buscar(
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) String accion,
            @RequestParam(required = false) String desde,
            @RequestParam(required = false) String hasta,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "50") int tamanio) {

        int paginaSegura = Math.max(0, pagina);
        int tamanioSeguro = Math.max(1, Math.min(tamanio, 200));

        return auditoriaService.buscar(
                        aInicioDeDia(desde),
                        aFinDeDia(hasta),
                        usuario,
                        parseAccion(accion),
                        PageRequest.of(paginaSegura, tamanioSeguro))
                .map(AuditoriaResponse::desde)
                .getContent();
    }

    private AccionAuditoria parseAccion(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        try {
            return AccionAuditoria.valueOf(valor.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private LocalDateTime aInicioDeDia(String fecha) {
        try {
            return fecha == null || fecha.isBlank()
                    ? null
                    : LocalDate.parse(fecha).atStartOfDay();
        } catch (Exception ex) {
            return null;
        }
    }

    private LocalDateTime aFinDeDia(String fecha) {
        try {
            return fecha == null || fecha.isBlank()
                    ? null
                    : LocalDate.parse(fecha).atTime(23, 59, 59);
        } catch (Exception ex) {
            return null;
        }
    }
}
