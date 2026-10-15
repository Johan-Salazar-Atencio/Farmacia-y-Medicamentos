package com.farmacia.medicamentos.controller;

import com.farmacia.medicamentos.dto.CategoriaForm;
import com.farmacia.medicamentos.entity.CategoriaMedicamento;
import com.farmacia.medicamentos.exception.BusinessException;
import com.farmacia.medicamentos.service.CategoriaMedicamentoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaMedicamentoService service;

    public CategoriaController(CategoriaMedicamentoService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", service.listarTodas());
        return "categorias/list";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("form", new CategoriaForm());
        return "categorias/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        CategoriaMedicamento c = service.obtenerPorId(id);
        CategoriaForm form = new CategoriaForm();
        form.setId(c.getId());
        form.setNombre(c.getNombre());
        form.setDescripcion(c.getDescripcion());
        model.addAttribute("form", form);
        return "categorias/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") CategoriaForm form, BindingResult resultado,
                          Model model, RedirectAttributes ra) {
        if (resultado.hasErrors()) {
            return "categorias/form";
        }
        try {
            if (form.getId() == null) {
                service.crear(form);
                ra.addFlashAttribute("mensaje", "Categoría registrada correctamente");
            } else {
                service.actualizar(form.getId(), form);
                ra.addFlashAttribute("mensaje", "Categoría actualizada correctamente");
            }
        } catch (BusinessException ex) {
            model.addAttribute("error", ex.getMessage());
            return "categorias/form";
        }
        return "redirect:/categorias";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, RedirectAttributes ra) {
        service.cambiarEstado(id);
        ra.addFlashAttribute("mensaje", "Estado de la categoría actualizado");
        return "redirect:/categorias";
    }
}
