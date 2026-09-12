package com.farmacia.controller;

import com.farmacia.dto.ProveedorForm;
import com.farmacia.dto.ProveedorResponse;
import com.farmacia.entity.Estado;
import com.farmacia.service.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST de Proveedor. El DELETE es lógico: cambia el estado a INACTIVO
 * (o de vuelta a ACTIVO) usando cambiarEstado(), sin borrar la fila de la BD.
 */
@RestController
@RequestMapping("/api/proveedores")
public class ProveedorRestController {

    private final ProveedorService proveedorService;

    public ProveedorRestController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    /**
     * GET /api/proveedores           -> todos
     * GET /api/proveedores?estado=ACTIVO
     */
    @GetMapping
    public List<ProveedorResponse> listar(@RequestParam(required = false) Estado estado) {
        var proveedores = (estado == null)
                ? proveedorService.listarTodos()
                : proveedorService.listarTodos().stream()
                .filter(p -> p.getEstado() == estado)
                .toList();

        return proveedores.stream()
                .map(ProveedorResponse::desde)
                .toList();
    }

    /**
     * GET /api/proveedores/{id}
     */
    @GetMapping("/{id}")
    public ProveedorResponse obtenerPorId(@PathVariable Long id) {
        return ProveedorResponse.desde(proveedorService.obtenerPorId(id));
    }

    /**
     * POST /api/proveedores
     * Body JSON:
     * {
     *   "ruc": "20123456789",
     *   "razonSocial": "Distribuidora Salud SAC",
     *   "telefono": "999888777",
     *   "email": "contacto@distsalud.com",
     *   "direccion": "Av. Siempre Viva 123"
     * }
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProveedorResponse crear(@Valid @RequestBody ProveedorForm form) {
        return ProveedorResponse.desde(proveedorService.crear(form));
    }

    /**
     * PUT /api/proveedores/{id}
     * Body JSON igual al de creación (el id de la URL manda, no el del body).
     */
    @PutMapping("/{id}")
    public ProveedorResponse actualizar(@PathVariable Long id,
                                        @Valid @RequestBody ProveedorForm form) {
        return ProveedorResponse.desde(proveedorService.actualizar(id, form));
    }

    /**
     * DELETE /api/proveedores/{id}
     * Borrado lógico: alterna el estado (ACTIVO <-> INACTIVO). Devuelve el
     * recurso actualizado para que en Postman se vea el nuevo estado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ProveedorResponse> cambiarEstado(@PathVariable Long id) {
        proveedorService.cambiarEstado(id);
        return ResponseEntity.ok(ProveedorResponse.desde(proveedorService.obtenerPorId(id)));
    }
}
