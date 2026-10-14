package com.farmacia.controller;

import com.farmacia.dto.LoteMedicamentoForm;
import com.farmacia.entity.Estado;
import com.farmacia.exception.BusinessException;
import com.farmacia.exception.ResourceNotFoundException;
import com.farmacia.service.LoteMedicamentoService;
import com.farmacia.service.MedicamentoService;
import com.farmacia.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/lotes")
public class LoteMedicamentoController {

    private final LoteMedicamentoService loteService;
    private final MedicamentoService medicamentoService;
    private final ProveedorService proveedorService;

    public LoteMedicamentoController(LoteMedicamentoService loteService,
                                      MedicamentoService medicamentoService,
                                      ProveedorService proveedorService) {
        this.loteService = loteService;
        this.medicamentoService = medicamentoService;
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("lotes", loteService.listarActivosVigentes());
        return "lotes/list";
    }

    @GetMapping("/nuevo")
    public String nuevoFormulario(Model model) {
        model.addAttribute("form", new LoteMedicamentoForm());
        cargarListasParaFormulario(model);
        return "lotes/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") LoteMedicamentoForm form,
                           BindingResult resultado,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            cargarListasParaFormulario(model);
            return "lotes/form";
        }
        try {
            loteService.registrar(form);
            redirectAttributes.addFlashAttribute("mensaje",
                    "Lote registrado correctamente. El stock del medicamento fue actualizado.");
            return "redirect:/lotes";
        } catch (BusinessException | ResourceNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            cargarListasParaFormulario(model);
            return "lotes/form";
        }
    }

    @PostMapping("/{id}/anular")
    public String anular(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        loteService.anular(id);
        redirectAttributes.addFlashAttribute("mensaje", "Estado del lote actualizado y stock ajustado");
        return "redirect:/lotes";
    }

    private void cargarListasParaFormulario(Model model) {
        model.addAttribute("medicamentos", medicamentoService.buscar(Estado.ACTIVO, null));
        model.addAttribute("proveedores", proveedorService.listarActivos());
    }
}
