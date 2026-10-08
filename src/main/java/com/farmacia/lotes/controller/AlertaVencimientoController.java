package com.farmacia.controller;

import com.farmacia.service.LoteMedicamentoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/alertas")
public class AlertaVencimientoController {

    private final LoteMedicamentoService loteService;

    public AlertaVencimientoController(LoteMedicamentoService loteService) {
        this.loteService = loteService;
    }

    @GetMapping
    public String verAlertas(Model model) {
        var vencidos = loteService.listarVencidos();
        var proximos30 = loteService.listarPorVencerEnRango(0, 30);
        var proximos60 = loteService.listarPorVencerEnRango(31, 60);

        model.addAttribute("vencidos", vencidos);
        model.addAttribute("proximos30", proximos30);
        model.addAttribute("proximos60", proximos60);
        model.addAttribute("totalVencidos", vencidos.size());
        model.addAttribute("totalProximos30", proximos30.size());
        model.addAttribute("totalProximos60", proximos60.size());
        return "alertas/list";
    }
}
