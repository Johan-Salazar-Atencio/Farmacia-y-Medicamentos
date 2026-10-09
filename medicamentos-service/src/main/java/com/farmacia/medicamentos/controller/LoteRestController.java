package com.farmacia.medicamentos.controller;

import com.farmacia.medicamentos.dto.LoteForm;
import com.farmacia.medicamentos.dto.LoteResponse;
import com.farmacia.medicamentos.service.LoteMedicamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lotes")
public class LoteRestController {

    private final LoteMedicamentoService service;

    public LoteRestController(LoteMedicamentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<LoteResponse> listar(@RequestParam(required = false) Long medicamentoId) {
        return service.buscar(medicamentoId).stream().map(LoteResponse::desde).toList();
    }

    @GetMapping("/{id}")
    public LoteResponse obtener(@PathVariable Long id) {
        return LoteResponse.desde(service.obtenerPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoteResponse crear(@Valid @RequestBody LoteForm form) {
        return LoteResponse.desde(service.crear(form));
    }

    @PutMapping("/{id}")
    public LoteResponse actualizar(@PathVariable Long id, @Valid @RequestBody LoteForm form) {
        return LoteResponse.desde(service.actualizar(id, form));
    }

    /** Eliminación física del lote; descuenta su cantidad del stock. */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
