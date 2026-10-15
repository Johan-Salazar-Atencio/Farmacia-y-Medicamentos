package com.farmacia.medicamentos.controller;

import com.farmacia.medicamentos.service.AuditoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auditoria")
public class AuditoriaController {

    private final AuditoriaService service;

    public AuditoriaController(AuditoriaService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String entidad, Model model) {
        model.addAttribute("registros", service.buscar(entidad));
        model.addAttribute("entidadSeleccionada", entidad);
        return "auditoria/list";
    }
}
