package com.farmacia.controller;

import com.farmacia.dto.CategoriaMedicamentoForm;
import com.farmacia.exception.BusinessException;
import com.farmacia.service.CategoriaMedicamentoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categorias")
public class CategoriaMedicamentoController {

    private final CategoriaMedicamentoService categoriaService;

    public CategoriaMedicamentoController(CategoriaMedicamentoService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "categorias/list";
    }

    @GetMapping("/nueva")
    public String nuevoFormulario(Model model) {
        model.addAttribute("form", new CategoriaMedicamentoForm());
        return "categorias/form";
    }

    @GetMapping("/{id}/editar")
    public String editarFormulario(@PathVariable Long id, Model model) {
        var categoria = categoriaService.obtenerPorId(id);
        CategoriaMedicamentoForm form = new CategoriaMedicamentoForm();
        form.setId(categoria.getId());
        form.setNombre(categoria.getNombre());
        form.setDescripcion(categoria.getDescripcion());
        model.addAttribute("form", form);
        return "categorias/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") CategoriaMedicamentoForm form,
                          BindingResult resultado,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            return "categorias/form";
        }
        try {
            if (form.getId() == null) {
                categoriaService.crear(form);
                redirectAttributes.addFlashAttribute("mensaje", "Categoría registrada correctamente");
            } else {
                categoriaService.actualizar(form.getId(), form);
                redirectAttributes.addFlashAttribute("mensaje", "Categoría actualizada correctamente");
            }
        } catch (BusinessException ex) {
            model.addAttribute("error", ex.getMessage());
            return "categorias/form";
        }
        return "redirect:/categorias";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        categoriaService.cambiarEstado(id);
        redirectAttributes.addFlashAttribute("mensaje", "Estado de la categoría actualizado");
        return "redirect:/categorias";
    }
}
