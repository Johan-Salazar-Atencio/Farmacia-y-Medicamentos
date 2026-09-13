package com.farmacia.controller;

import com.farmacia.dto.MedicamentoForm;
import com.farmacia.exception.BusinessException;
import com.farmacia.repository.CategoriaMedicamentoRepository;
import com.farmacia.service.MedicamentoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/medicamentos")
public class MedicamentoController {

    private final MedicamentoService medicamentoService;
    private final CategoriaMedicamentoRepository categoriaRepository;

    public MedicamentoController(MedicamentoService medicamentoService,
                                 CategoriaMedicamentoRepository categoriaRepository) {
        this.medicamentoService = medicamentoService;
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("medicamentos", medicamentoService.listarTodos());
        return "medicamentos/list";
    }

    @GetMapping("/nuevo")
    public String nuevoFormulario(Model model) {
        model.addAttribute("form", new MedicamentoForm());
        model.addAttribute("categorias", categoriaRepository.findAllByOrderByNombreAsc());
        return "medicamentos/form";
    }

    @GetMapping("/{id}/editar")
    public String editarFormulario(@PathVariable Long id, Model model) {
        var medicamento = medicamentoService.obtenerPorId(id);
        MedicamentoForm form = new MedicamentoForm();
        form.setId(medicamento.getId());
        form.setCodigo(medicamento.getCodigo());
        form.setNombre(medicamento.getNombre());
        form.setDescripcion(medicamento.getDescripcion());
        form.setPrecioVenta(medicamento.getPrecioVenta());
        form.setCategoriaId(medicamento.getCategoria() != null ? medicamento.getCategoria().getId() : null);
        model.addAttribute("form", form);
        model.addAttribute("categorias", categoriaRepository.findAllByOrderByNombreAsc());
        return "medicamentos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") MedicamentoForm form,
                          BindingResult resultado, Model model,
                          RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            model.addAttribute("categorias", categoriaRepository.findAllByOrderByNombreAsc());
            return "medicamentos/form";
        }
        try {
            if (form.getId() == null) {
                medicamentoService.crear(form);
                redirectAttributes.addFlashAttribute("mensaje", "Medicamento registrado correctamente");
            } else {
                medicamentoService.actualizar(form.getId(), form);
                redirectAttributes.addFlashAttribute("mensaje", "Medicamento actualizado correctamente");
            }
        } catch (BusinessException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("categorias", categoriaRepository.findAllByOrderByNombreAsc());
            return "medicamentos/form";
        }
        return "redirect:/medicamentos";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        medicamentoService.cambiarEstado(id);
        redirectAttributes.addFlashAttribute("mensaje", "Estado del medicamento actualizado");
        return "redirect:/medicamentos";
    }
}