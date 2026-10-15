package com.farmacia.controller;

import com.farmacia.dto.MedicamentoForm;
import com.farmacia.dto.MedicamentoResponse;
import com.farmacia.entity.Estado;
import com.farmacia.service.MedicamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST de Medicamento. El DELETE es lógico: cambia el estado a INACTIVO
 * (o de vuelta a ACTIVO) usando cambiarEstado(), sin borrar la fila de la BD
 * ni el historial de lotes asociados.
 */
@RestController
@RequestMapping("/api/medicamentos")
public class MedicamentoRestController {

    private final MedicamentoService medicamentoService;

    public MedicamentoRestController(MedicamentoService medicamentoService) {
        this.medicamentoService = medicamentoService;
    }

    /**
     * GET /api/medicamentos
     * GET /api/medicamentos?estado=ACTIVO
     * GET /api/medicamentos?categoriaId=3
     * GET /api/medicamentos?estado=ACTIVO&categoriaId=3
     */
    @GetMapping
    public List<MedicamentoResponse> listar(@RequestParam(required = false) Estado estado,
                                            @RequestParam(required = false) Long categoriaId) {
        return medicamentoService.buscar(estado, categoriaId)
                .stream()
                .map(MedicamentoResponse::desde)
                .toList();
    }

    /**
     * GET /api/medicamentos/{id}
     */
    @GetMapping("/{id}")
    public MedicamentoResponse obtenerPorId(@PathVariable Long id) {
        return MedicamentoResponse.desde(medicamentoService.obtenerPorId(id));
    }

    /**
     * POST /api/medicamentos
     * Body JSON:
     * {
     *   "nombre": "Paracetamol",
     *   "descripcion": "Analgésico y antipirético",
     *   "presentacion": "Tabletas",
     *   "concentracion": "500mg",
     *   "precioVenta": 5.50,
     *   "stockMinimo": 20,
     *   "categoriaId": 1
     * }
     * Nota: categoriaId debe existir (créala primero con POST /api/categorias).
     * El código se genera en el servicio; el stock inicia en 0 y solo sube al
     * registrar lotes.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicamentoResponse crear(@Valid @RequestBody MedicamentoForm form) {
        return MedicamentoResponse.desde(medicamentoService.crear(form));
    }

    /**
     * PUT /api/medicamentos/{id}
     * Body JSON igual al de creación (el id de la URL manda, no el del body).
     */
    @PutMapping("/{id}")
    public MedicamentoResponse actualizar(@PathVariable Long id,
                                          @Valid @RequestBody MedicamentoForm form) {
        return MedicamentoResponse.desde(medicamentoService.actualizar(id, form));
    }

    /**
     * DELETE /api/medicamentos/{id}
     * Borrado lógico: alterna el estado (ACTIVO <-> INACTIVO). Devuelve el
     * recurso actualizado para que en Postman se vea el nuevo estado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MedicamentoResponse> cambiarEstado(@PathVariable Long id) {
        medicamentoService.cambiarEstado(id);
        return ResponseEntity.ok(MedicamentoResponse.desde(medicamentoService.obtenerPorId(id)));
    }
}
