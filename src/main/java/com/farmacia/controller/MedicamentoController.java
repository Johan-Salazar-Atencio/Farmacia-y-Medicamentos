package com.farmacia.controller;

import com.farmacia.dto.MedicamentoForm;
import com.farmacia.entity.Estado;
import com.farmacia.exception.BusinessException;
import com.farmacia.service.CategoriaMedicamentoService;
import com.farmacia.service.MedicamentoService;
import org.springframework.util.StringUtils;
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
    private final CategoriaMedicamentoService categoriaService;

    public MedicamentoController(MedicamentoService medicamentoService,
                                  CategoriaMedicamentoService categoriaService) {
        this.medicamentoService = medicamentoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String estado,
                          @RequestParam(required = false) String categoriaId,
                          Model model) {
        Estado estadoFiltro = parseEstado(estado);
        Long categoriaFiltro = parseLong(categoriaId);
        model.addAttribute("medicamentos", medicamentoService.buscar(estadoFiltro, categoriaFiltro));
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("estadoSeleccionado", estadoFiltro);
        model.addAttribute("categoriaSeleccionada", categoriaFiltro);
        return "medicamentos/list";
    }

    private Estado parseEstado(String valor) {
        if (StringUtils.hasText(valor)) {
            try {
                return Estado.valueOf(valor.toUpperCase());
            } catch (IllegalArgumentException ignorado) {
            }
        }
        return null;
    }

    private Long parseLong(String valor) {
        if (StringUtils.hasText(valor)) {
            try {
                return Long.valueOf(valor);
            } catch (NumberFormatException ignorado) {
            }
        }
        return null;
    }

    @GetMapping("/nuevo")
    public String nuevoFormulario(Model model) {
        model.addAttribute("form", new MedicamentoForm());
        model.addAttribute("categorias", categoriaService.listarActivas());
        return "medicamentos/form";
    }

    @GetMapping("/{id}/editar")
    public String editarFormulario(@PathVariable Long id, Model model) {
        var medicamento = medicamentoService.obtenerPorId(id);
        MedicamentoForm form = new MedicamentoForm();
        form.setId(medicamento.getId());
        form.setNombre(medicamento.getNombre());
        form.setDescripcion(medicamento.getDescripcion());
        form.setPresentacion(medicamento.getPresentacion());
        form.setConcentracion(medicamento.getConcentracion());
        form.setPrecioVenta(medicamento.getPrecioVenta());
        form.setStockMinimo(medicamento.getStockMinimo());
        form.setCategoriaId(medicamento.getCategoria().getId());

        model.addAttribute("form", form);
        model.addAttribute("categorias", categoriaService.listarActivas());
        model.addAttribute("codigoActual", medicamento.getCodigo());
        model.addAttribute("stockActual", medicamento.getStock());
        return "medicamentos/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") MedicamentoForm form,
                           BindingResult resultado,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarActivas());
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
            model.addAttribute("categorias", categoriaService.listarActivas());
            model.addAttribute("error", ex.getMessage());
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
