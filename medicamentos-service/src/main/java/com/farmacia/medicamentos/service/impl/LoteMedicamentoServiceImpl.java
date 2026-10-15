package com.farmacia.medicamentos.service.impl;

import com.farmacia.medicamentos.audit.Auditable;
import com.farmacia.medicamentos.audit.Operacion;
import com.farmacia.medicamentos.dto.LoteForm;
import com.farmacia.medicamentos.entity.LoteMedicamento;
import com.farmacia.medicamentos.entity.Medicamento;
import com.farmacia.medicamentos.exception.BusinessException;
import com.farmacia.medicamentos.exception.ResourceNotFoundException;
import com.farmacia.medicamentos.repository.LoteMedicamentoRepository;
import com.farmacia.medicamentos.repository.MedicamentoRepository;
import com.farmacia.medicamentos.service.LoteMedicamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CRUD de lotes manteniendo la integridad con el stock del medicamento:
 * registrar suma, editar ajusta la diferencia y eliminar resta. El stock nunca queda negativo.
 */
@Service
@Transactional
public class LoteMedicamentoServiceImpl implements LoteMedicamentoService {

    private final LoteMedicamentoRepository loteRepository;
    private final MedicamentoRepository medicamentoRepository;

    public LoteMedicamentoServiceImpl(LoteMedicamentoRepository loteRepository,
                                      MedicamentoRepository medicamentoRepository) {
        this.loteRepository = loteRepository;
        this.medicamentoRepository = medicamentoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoteMedicamento> buscar(Long medicamentoId) {
        return loteRepository.buscar(medicamentoId);
    }

    @Override
    @Transactional(readOnly = true)
    public LoteMedicamento obtenerPorId(Long id) {
        return loteRepository.findByIdConMedicamento(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el lote con id " + id));
    }

    @Override
    @Auditable(entidad = "LoteMedicamento", operacion = Operacion.REGISTRO)
    public LoteMedicamento crear(LoteForm form) {
        Medicamento med = medicamentoRepository.findById(form.getMedicamentoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el medicamento con id " + form.getMedicamentoId()));
        String numero = form.getNumeroLote().trim();
        if (loteRepository.existsByMedicamentoIdAndNumeroLoteIgnoreCase(med.getId(), numero)) {
            throw new BusinessException("El medicamento ya tiene un lote con número '" + numero + "'");
        }
        LoteMedicamento lote = new LoteMedicamento();
        lote.setMedicamento(med);
        lote.setNumeroLote(numero);
        lote.setFechaVencimiento(form.getFechaVencimiento());
        lote.setCantidad(form.getCantidad());
        lote.setProveedorId(form.getProveedorId());
        med.setStock(med.getStock() + form.getCantidad());
        medicamentoRepository.save(med);
        return loteRepository.save(lote);
    }

    @Override
    @Auditable(entidad = "LoteMedicamento", operacion = Operacion.MODIFICACION)
    public LoteMedicamento actualizar(Long id, LoteForm form) {
        LoteMedicamento lote = obtenerPorId(id);
        Medicamento med = lote.getMedicamento();
        int nuevoStock = med.getStock() - lote.getCantidad() + form.getCantidad();
        if (nuevoStock < 0) {
            throw new BusinessException("No se puede reducir la cantidad: el stock del medicamento quedaría negativo");
        }
        med.setStock(nuevoStock);
        lote.setNumeroLote(form.getNumeroLote().trim());
        lote.setFechaVencimiento(form.getFechaVencimiento());
        lote.setCantidad(form.getCantidad());
        lote.setProveedorId(form.getProveedorId());
        medicamentoRepository.save(med);
        return loteRepository.save(lote);
    }

    @Override
    @Auditable(entidad = "LoteMedicamento", operacion = Operacion.ELIMINACION)
    public void eliminar(Long id) {
        LoteMedicamento lote = obtenerPorId(id);
        Medicamento med = lote.getMedicamento();
        int nuevoStock = med.getStock() - lote.getCantidad();
        if (nuevoStock < 0) {
            throw new BusinessException("No se puede eliminar el lote: parte de su stock ya fue consumido");
        }
        med.setStock(nuevoStock);
        medicamentoRepository.save(med);
        loteRepository.delete(lote);
    }
}
