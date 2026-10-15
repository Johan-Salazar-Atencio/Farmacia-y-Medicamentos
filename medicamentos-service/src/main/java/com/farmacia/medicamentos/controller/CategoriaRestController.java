package com.farmacia.medicamentos.controller;

import com.farmacia.medicamentos.dto.CategoriaForm;
import com.farmacia.medicamentos.dto.CategoriaResponse;
import com.farmacia.medicamentos.service.CategoriaMedicamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaRestController {

    private final CategoriaMedicamentoService service;

    public CategoriaRestController(CategoriaMedicamentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoriaResponse> listar() {
        return service.listarTodas().stream().map(CategoriaResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public CategoriaResponse obtener(@PathVariable Long id) {
        return CategoriaResponse.desde(service.obtenerPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponse crear(@Valid @RequestBody CategoriaForm form) {
        return CategoriaResponse.desde(service.crear(form));
    }

    @PutMapping("/{id}")
    public CategoriaResponse actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaForm form) {
        return CategoriaResponse.desde(service.actualizar(id, form));
    }

    /** DELETE lógico: alterna ACTIVO/INACTIVO. */
    @DeleteMapping("/{id}")
    public CategoriaResponse cambiarEstado(@PathVariable Long id) {
        service.cambiarEstado(id);
        return CategoriaResponse.desde(service.obtenerPorId(id));
    }
}
