package com.farmacia.service.impl;

import com.farmacia.dto.LoteMedicamentoForm;
import com.farmacia.entity.Estado;
import com.farmacia.entity.LoteMedicamento;
import com.farmacia.entity.Medicamento;
import com.farmacia.entity.Proveedor;
import com.farmacia.exception.BusinessException;
import com.farmacia.exception.ResourceNotFoundException;
import com.farmacia.repository.LoteMedicamentoRepository;
import com.farmacia.repository.MedicamentoRepository;
import com.farmacia.repository.ProveedorRepository;
import com.farmacia.service.LoteMedicamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class LoteMedicamentoServiceImpl implements LoteMedicamentoService {

    private final LoteMedicamentoRepository loteRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final ProveedorRepository proveedorRepository;

    public LoteMedicamentoServiceImpl(LoteMedicamentoRepository loteRepository,
                                      MedicamentoRepository medicamentoRepository,
                                      ProveedorRepository proveedorRepository) {
        this.loteRepository = loteRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    public LoteMedicamento registrar(LoteMedicamentoForm form) {
        // RF regla 2 (defensa adicional a la validación @Future del DTO).
        if (!form.getFechaVencimiento().isAfter(LocalDate.now())) {
            throw new BusinessException("La fecha de vencimiento debe ser posterior a la fecha actual");
        }

        Medicamento medicamento = medicamentoRepository.findById(form.getMedicamentoId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el medicamento seleccionado"));
        Proveedor proveedor = proveedorRepository.findById(form.getProveedorId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el proveedor seleccionado"));

        if (medicamento.getEstado() == Estado.INACTIVO) {
            throw new BusinessException("No se puede registrar un lote para un medicamento inactivo");
        }
        if (proveedor.getEstado() == Estado.INACTIVO) {
            throw new BusinessException("No se puede registrar un lote con un proveedor inactivo");
        }

        LoteMedicamento lote = new LoteMedicamento();
        lote.setNumeroLote(form.getNumeroLote());
        lote.setMedicamento(medicamento);
        lote.setProveedor(proveedor);
        lote.setCantidad(form.getCantidad());
        lote.setFechaVencimiento(form.getFechaVencimiento());
        lote.setPrecioCompra(form.getPrecioCompra());
        LoteMedicamento loteGuardado = loteRepository.save(lote);

        // RF regla 3: registrar un lote actualiza el stock del medicamento.
        medicamento.setStock(medicamento.getStock() + form.getCantidad());
        medicamentoRepository.save(medicamento);

        return loteGuardado;
    }

    @Override
    @Transactional(readOnly = true)
    public LoteMedicamento obtenerPorId(Long id) {
        return loteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el lote con id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoteMedicamento> listarActivosVigentes() {
        return loteRepository.findByEstadoAndFechaVencimientoGreaterThanEqualOrderByFechaVencimientoAsc(
                Estado.ACTIVO, LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoteMedicamento> listarVencidos() {
        return loteRepository.findByEstadoAndFechaVencimientoBeforeOrderByFechaVencimientoDesc(
                Estado.ACTIVO, LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoteMedicamento> listarPorVencerEnRango(int diasDesde, int diasHasta) {
        LocalDate desde = LocalDate.now().plusDays(diasDesde);
        LocalDate hasta = LocalDate.now().plusDays(diasHasta);
        return loteRepository.findByEstadoAndFechaVencimientoBetweenOrderByFechaVencimientoAsc(
                Estado.ACTIVO, desde, hasta);
    }

    @Override
    public void anular(Long id) {
        LoteMedicamento lote = loteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el lote con id " + id));

        Medicamento medicamento = lote.getMedicamento();
        if (lote.getEstado() == Estado.ACTIVO) {
            // Al anular un lote activo se revierte el stock que había aportado.
            int nuevoStock = Math.max(0, medicamento.getStock() - lote.getCantidad());
            medicamento.setStock(nuevoStock);
            lote.setEstado(Estado.INACTIVO);
        } else {
            medicamento.setStock(medicamento.getStock() + lote.getCantidad());
            lote.setEstado(Estado.ACTIVO);
        }
        medicamentoRepository.save(medicamento);
        loteRepository.save(lote);
    }
}