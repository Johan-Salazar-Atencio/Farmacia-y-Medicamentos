
package com.farmacia.core.controller;

import com.farmacia.core.dto.UsuarioForm;
import com.farmacia.core.dto.UsuarioResponse;
import com.farmacia.core.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioRestController {

    private final UsuarioService usuarioService;

    public UsuarioRestController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        return usuarioService.listarTodos().stream()
                .map(UsuarioResponse::desde)
                .toList();
    }

    @GetMapping("/{id}")
    public UsuarioResponse obtenerPorId(@PathVariable Long id) {
        return UsuarioResponse.desde(
                usuarioService.obtenerPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse crear(
            @Valid @RequestBody UsuarioForm form) {
        return UsuarioResponse.desde(
                usuarioService.crear(form));
    }

    @PutMapping("/{id}")
    public UsuarioResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioForm form) {
        return UsuarioResponse.desde(
                usuarioService.actualizar(id, form));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UsuarioResponse> cambiarActivo(
            @PathVariable Long id) {
        usuarioService.cambiarActivo(id);

        return ResponseEntity.ok(
                UsuarioResponse.desde(
                        usuarioService.obtenerPorId(id)));
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Map<String, String>> cambiarPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> cuerpo) {
        usuarioService.cambiarPassword(
                id,
                cuerpo.get("password"),
                cuerpo.get("confirmarPassword"));

        return ResponseEntity.ok(
                Map.of("mensaje",
                        "Contraseña actualizada correctamente"));
    }
}
