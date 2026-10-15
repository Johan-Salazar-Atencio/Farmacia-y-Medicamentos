
package com.farmacia.core.controller;

import com.farmacia.core.dto.UsuarioForm;
import com.farmacia.core.exception.BusinessException;
import com.farmacia.core.repository.RolRepository;
import com.farmacia.core.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolRepository rolRepository;

    public UsuarioController(
            UsuarioService usuarioService,
            RolRepository rolRepository) {
        this.usuarioService = usuarioService;
        this.rolRepository = rolRepository;
    }

    @GetMapping
    public String listar(
            @RequestParam(defaultValue = "0") int pagina,
            Model model) {
        Page<?> usuarios = usuarioService.listarPaginado(
                PageRequest.of(Math.max(0, pagina), 10));

        model.addAttribute("usuarios", usuarios.getContent());
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("totalPaginas", usuarios.getTotalPages());

        return "usuarios/list";
    }

    @GetMapping("/nuevo")
    public String nuevoFormulario(Model model) {
        model.addAttribute("form", new UsuarioForm());
        prepararRoles(model);
        return "usuarios/form";
    }

    @GetMapping("/{id}/editar")
    public String editarFormulario(
            @PathVariable Long id,
            Model model) {
        var usuario = usuarioService.obtenerPorId(id);
        UsuarioForm form = new UsuarioForm();

        form.setId(usuario.getId());
        form.setUsername(usuario.getUsername());
        form.setNombre(usuario.getNombre());
        form.setEmail(usuario.getEmail());
        form.setActivo(usuario.getActivo());

        usuario.getRoles().forEach(
                rol -> form.getRoles().add(rol.getNombre()));

        model.addAttribute("form", form);
        prepararRoles(model);

        return "usuarios/form";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("form") UsuarioForm form,
            BindingResult resultado,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (resultado.hasErrors()) {
            prepararRoles(model);
            return "usuarios/form";
        }

        try {
            if (form.getId() == null) {
                usuarioService.crear(form);
                redirectAttributes.addFlashAttribute(
                        "mensaje", "Usuario registrado correctamente");
            } else {
                usuarioService.actualizar(form.getId(), form);
                redirectAttributes.addFlashAttribute(
                        "mensaje", "Usuario actualizado correctamente");
            }
        } catch (BusinessException ex) {
            prepararRoles(model);
            model.addAttribute("error", ex.getMessage());
            return "usuarios/form";
        }

        return "redirect:/usuarios";
    }

    @PostMapping("/{id}/activo")
    public String cambiarActivo(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            usuarioService.cambiarActivo(id);
            redirectAttributes.addFlashAttribute(
                    "mensaje", "Estado del usuario actualizado");
        } catch (BusinessException ex) {
            redirectAttributes.addFlashAttribute(
                    "error", ex.getMessage());
        }

        return "redirect:/usuarios";
    }

    private void prepararRoles(Model model) {
        model.addAttribute(
                "rolesDisponibles",
                rolRepository.findAllByOrderByNombreAsc());
    }
}
