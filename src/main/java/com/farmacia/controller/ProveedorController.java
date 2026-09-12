package com.farmacia.controller;

import com.farmacia.dto.ProveedorForm;
import com.farmacia.exception.BusinessException;
import com.farmacia.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("proveedores", proveedorService.listarTodos());
        return "proveedores/list";
    }

    @GetMapping("/nuevo")
    public String nuevoFormulario(Model model) {
        model.addAttribute("form", new ProveedorForm());
        return "proveedores/form";
    }

    @GetMapping("/{id}/editar")
    public String editarFormulario(@PathVariable Long id, Model model) {
        var proveedor = proveedorService.obtenerPorId(id);
        ProveedorForm form = new ProveedorForm();
        form.setId(proveedor.getId());
        form.setRuc(proveedor.getRuc());
        form.setRazonSocial(proveedor.getRazonSocial());
        form.setTelefono(proveedor.getTelefono());
        form.setEmail(proveedor.getEmail());
        form.setDireccion(proveedor.getDireccion());
        model.addAttribute("form", form);
        return "proveedores/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("form") ProveedorForm form,
                          BindingResult resultado,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            return "proveedores/form";
        }
        try {
            if (form.getId() == null) {
                proveedorService.crear(form);
                redirectAttributes.addFlashAttribute("mensaje", "Proveedor registrado correctamente");
            } else {
                proveedorService.actualizar(form.getId(), form);
                redirectAttributes.addFlashAttribute("mensaje", "Proveedor actualizado correctamente");
            }
        } catch (BusinessException ex) {
            model.addAttribute("error", ex.getMessage());
            return "proveedores/form";
        }
        return "redirect:/proveedores";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        proveedorService.cambiarEstado(id);
        redirectAttributes.addFlashAttribute("mensaje", "Estado del proveedor actualizado");
        return "redirect:/proveedores";
    }
}
