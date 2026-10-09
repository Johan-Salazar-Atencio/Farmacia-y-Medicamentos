
package com.farmacia.core.controller;

import com.farmacia.core.entity.AccionAuditoria;
import com.farmacia.core.service.AuditoriaService;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/auditorias")
@PreAuthorize("hasRole('ADMIN')")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    public String listar(
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta,
            @RequestParam(required = false) String usuario,
            @RequestParam(required = false) String accion,
            @RequestParam(defaultValue = "0") int pagina,
            Model model) {

        var registros = auditoriaService.buscar(
                fecha(fechaDesde, false),
                fecha(fechaHasta, true),
                usuario,
                parseAccion(accion),
                PageRequest.of(Math.max(0, pagina), 25)
        );

        model.addAttribute("registros", registros.getContent());
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPaginas", registros.getTotalPages());
        model.addAttribute("filtroDesde", fechaDesde == null ? "" : fechaDesde);
        model.addAttribute("filtroHasta", fechaHasta == null ? "" : fechaHasta);
        model.addAttribute("filtroUsuario", usuario == null ? "" : usuario);
        model.addAttribute("filtroAccion", accion == null ? "" : accion);
        model.addAttribute("acciones", AccionAuditoria.values());

        return "auditorias/list";
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

    private LocalDateTime fecha(String valor, boolean fin) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        try {
            LocalDate dia = LocalDate.parse(valor);

            return fin
                    ? dia.atTime(23, 59, 59)
                    : dia.atStartOfDay();
        } catch (Exception ex) {
            return null;
        }
    }
}
