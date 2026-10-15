package com.farmacia.controller;

import com.farmacia.dto.CategoriaMedicamentoForm;
import com.farmacia.dto.CategoriaMedicamentoResponse;
import com.farmacia.entity.Estado;
import com.farmacia.service.CategoriaMedicamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST de CategoriaMedicamento. El DELETE es lógico: cambia el estado
 * a INACTIVO (o de vuelta a ACTIVO si ya estaba inactivo) usando cambiarEstado(),
 * igual que en el resto del sistema — nunca borra la fila de la base de datos.
 */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaMedicamentoRestController {

    private final CategoriaMedicamentoService categoriaService;

    public CategoriaMedicamentoRestController(CategoriaMedicamentoService categoriaService) {
        this.categoriaService = categoriaService;
    }

    /**
     * GET /api/categorias            -> todas
     * GET /api/categorias?estado=ACTIVO
     */
    @GetMapping
    public List<CategoriaMedicamentoResponse> listar(@RequestParam(required = false) Estado estado) {
        var categorias = (estado == null)
                ? categoriaService.listarTodas()
                : categoriaService.listarTodas().stream()
                    .filter(c -> c.getEstado() == estado)
                    .toList();

        return categorias.stream()
                .map(CategoriaMedicamentoResponse::desde)
                .toList();
    }

    /**
     * GET /api/categorias/{id}
     */
    @GetMapping("/{id}")
    public CategoriaMedicamentoResponse obtenerPorId(@PathVariable Long id) {
        return CategoriaMedicamentoResponse.desde(categoriaService.obtenerPorId(id));
    }

    /**
     * POST /api/categorias
     * Body JSON:
     * { "nombre": "Analgésicos", "descripcion": "Medicamentos para el dolor" }
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaMedicamentoResponse crear(@Valid @RequestBody CategoriaMedicamentoForm form) {
        return CategoriaMedicamentoResponse.desde(categoriaService.crear(form));
    }

    /**
     * PUT /api/categorias/{id}
     * Body JSON igual al de creación (el id de la URL manda, no el del body).
     */
    @PutMapping("/{id}")
    public CategoriaMedicamentoResponse actualizar(@PathVariable Long id,
                                                    @Valid @RequestBody CategoriaMedicamentoForm form) {
        return CategoriaMedicamentoResponse.desde(categoriaService.actualizar(id, form));
    }

    /**
     * DELETE /api/categorias/{id}
     * Borrado lógico: alterna el estado (ACTIVO <-> INACTIVO). Devuelve el
     * recurso actualizado para que en Postman se vea el nuevo estado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoriaMedicamentoResponse> cambiarEstado(@PathVariable Long id) {
        categoriaService.cambiarEstado(id);
        return ResponseEntity.ok(CategoriaMedicamentoResponse.desde(categoriaService.obtenerPorId(id)));
    }
}
