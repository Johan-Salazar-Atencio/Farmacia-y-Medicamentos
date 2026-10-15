package com.farmacia.controller;

import com.farmacia.dto.LoteMedicamentoForm;
import com.farmacia.dto.LoteMedicamentoResponse;
import com.farmacia.service.LoteMedicamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST de LoteMedicamento.
 *
 * Sin PUT: un lote afecta el stock del medicamento al registrarse (RF regla 3),
 * así que no se permite editarlo. Para corregir un error, anula el lote incorrecto
 * (DELETE) y registra uno nuevo (POST).
 *
 * DELETE es lógico (anular), no borra la fila de la base de datos.
 */
@RestController
@RequestMapping("/api/lotes")
public class LoteMedicamentoRestController {

    private final LoteMedicamentoService loteService;

    public LoteMedicamentoRestController(LoteMedicamentoService loteService) {
        this.loteService = loteService;
    }

    /**
     * GET /api/lotes                         -> activos y vigentes (no vencidos)
     * GET /api/lotes?tipo=vencidos            -> lotes ya vencidos
     * GET /api/lotes?tipo=porVencer&diasDesde=0&diasHasta=30 -> rango de vencimiento
     */
    @GetMapping
    public List<LoteMedicamentoResponse> listar(
            @RequestParam(required = false, defaultValue = "vigentes") String tipo,
            @RequestParam(required = false, defaultValue = "0") int diasDesde,
            @RequestParam(required = false, defaultValue = "30") int diasHasta) {

        List<com.farmacia.entity.LoteMedicamento> lotes = switch (tipo) {
            case "vencidos" -> loteService.listarVencidos();
            case "porVencer" -> loteService.listarPorVencerEnRango(diasDesde, diasHasta);
            default -> loteService.listarActivosVigentes();
        };

        return lotes.stream()
                .map(LoteMedicamentoResponse::desde)
                .toList();
    }

    /**
     * GET /api/lotes/{id}
     * Requiere que agregues obtenerPorId(Long id) a LoteMedicamentoService.
     */
    @GetMapping("/{id}")
    public LoteMedicamentoResponse obtenerPorId(@PathVariable Long id) {
        return LoteMedicamentoResponse.desde(loteService.obtenerPorId(id));
    }

    /**
     * POST /api/lotes
     * Body JSON:
     * {
     *   "medicamentoId": 1,
     *   "proveedorId": 1,
     *   "numeroLote": "L-2026-001",
     *   "cantidad": 100,
     *   "fechaVencimiento": "2027-06-30",
     *   "precioCompra": 3.20
     * }
     * Nota: medicamentoId y proveedorId deben existir. fechaVencimiento debe ser
     * futura (RF regla 2). Al registrar, suma 'cantidad' al stock del medicamento.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoteMedicamentoResponse registrar(@Valid @RequestBody LoteMedicamentoForm form) {
        return LoteMedicamentoResponse.desde(loteService.registrar(form));
    }

    /**
     * DELETE /api/lotes/{id}
     * Anula el lote (borrado lógico). Devuelve el recurso actualizado con
     * estado INACTIVO para confirmar en Postman.
     */
    @DeleteMapping("/{id}")
    public LoteMedicamentoResponse anular(@PathVariable Long id) {
        loteService.anular(id);
        return LoteMedicamentoResponse.desde(loteService.obtenerPorId(id));
    }
}
