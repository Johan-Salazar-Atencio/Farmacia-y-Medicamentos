package com.farmacia.medicamentos.controller;

import com.farmacia.medicamentos.dto.LoteForm;
import com.farmacia.medicamentos.entity.Estado;
import com.farmacia.medicamentos.entity.LoteMedicamento;
import com.farmacia.medicamentos.exception.BusinessException;
import com.farmacia.medicamentos.service.LoteMedicamentoService;
import com.farmacia.medicamentos.service.MedicamentoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/lotes")
public class LoteController {

    private final LoteMedicamentoService loteService;
    private final MedicamentoService medicamentoService;

    public LoteController(LoteMedicamentoService loteService, MedicamentoService medicamentoService) {
        this.loteService = loteService;
        this.medicamentoService = medicamentoService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) Long medicamentoId, Model model) {
        model.addAttribute("lotes", loteService.buscar(medicamentoId));
        model.addAttribute("medicamentos", medicamentoService.buscar(null, null));
        model.addAttribute("medicamentoSeleccionado", medicamentoId);
        return "lotes/list";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("form", new LoteForm());
        model.addAttribute("medicamentos", medicamentoService.buscar(Estado.ACTIVO, null));
        return "lotes/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        LoteMedicamento l = loteService.obtenerPorId(id);
        LoteForm form = new LoteForm();
        form.setId(l.getId());
        form.setMedicamentoId(l.getMedicamento().getId());
        form.setNumeroLote(l.getNumeroLote());
        form.setFechaVencimiento(l.getFechaVencimiento());
        form.setCantidad(l.getCantidad());
        form.setProveedorId(l.getProveedorId());
        model.addAttribute("form", form);
        model.addAttribute("medicamentos", medicamentoService.buscar(null, null));
        return "lotes/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") LoteForm form, BindingResult resultado,
                          Model model, RedirectAttributes ra) {
        if (resultado.hasErrors()) {
            model.addAttribute("medicamentos", medicamentoService.buscar(null, null));
            return "lotes/form";
        }
        try {
            if (form.getId() == null) {
                loteService.crear(form);
                ra.addFlashAttribute("mensaje", "Lote registrado correctamente (stock actualizado)");
            } else {
                loteService.actualizar(form.getId(), form);
                ra.addFlashAttribute("mensaje", "Lote actualizado correctamente (stock ajustado)");
            }
        } catch (BusinessException ex) {
            model.addAttribute("medicamentos", medicamentoService.buscar(null, null));
            model.addAttribute("error", ex.getMessage());
            return "lotes/form";
        }
        return "redirect:/lotes";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        try {
            loteService.eliminar(id);
            ra.addFlashAttribute("mensaje", "Lote eliminado (stock descontado)");
        } catch (BusinessException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/lotes";
    }
}
