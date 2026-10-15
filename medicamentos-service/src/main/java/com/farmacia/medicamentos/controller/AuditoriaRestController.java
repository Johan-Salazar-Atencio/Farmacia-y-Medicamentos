package com.farmacia.medicamentos.controller;

import com.farmacia.medicamentos.dto.AuditoriaResponse;
import com.farmacia.medicamentos.service.AuditoriaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaRestController {

    private final AuditoriaService service;

    public AuditoriaRestController(AuditoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<AuditoriaResponse> listar(@RequestParam(required = false) String entidad) {
        return service.buscar(entidad).stream().map(AuditoriaResponse::desde).toList();
    }
}
