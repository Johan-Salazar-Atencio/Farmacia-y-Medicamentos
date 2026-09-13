package com.farmacia.service.impl;

import com.farmacia.dto.MedicamentoForm;
import com.farmacia.entity.CategoriaMedicamento;
import com.farmacia.entity.Estado;
import com.farmacia.entity.Medicamento;
import com.farmacia.exception.BusinessException;
import com.farmacia.exception.ResourceNotFoundException;
import com.farmacia.repository.CategoriaMedicamentoRepository;
import com.farmacia.repository.MedicamentoRepository;
import com.farmacia.service.MedicamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class MedicamentoServiceImpl implements MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;
    private final CategoriaMedicamentoRepository categoriaRepository;

    public MedicamentoServiceImpl(MedicamentoRepository medicamentoRepository,
                                  CategoriaMedicamentoRepository categoriaRepository) {
        this.medicamentoRepository = medicamentoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medicamento> listarTodos() {
        return medicamentoRepository.findAllByOrderByNombreAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medicamento> listarActivos() {
        return medicamentoRepository.findByEstadoOrderByNombreAsc(Estado.ACTIVO);
    }

    @Override
    @Transactional(readOnly = true)
    public Medicamento obtenerPorId(Long id) {
        return medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el medicamento con id " + id));
    }

    @Override
    public Medicamento crear(MedicamentoForm form) {
        // RF-FAR-06: valida que el código no exista ya
        if (medicamentoRepository.existsByCodigo(form.getCodigo())) {
            throw new BusinessException("Ya existe un medicamento registrado con ese código");
        }
        Medicamento medicamento = new Medicamento();
        aplicarDatos(medicamento, form);
        return medicamentoRepository.save(medicamento);
    }

    @Override
    public Medicamento actualizar(Long id, MedicamentoForm form) {
        Medicamento medicamento = obtenerPorId(id);
        // RF-FAR-06: al editar, valida que el código no choque con OTRO medicamento
        if (medicamentoRepository.existsByCodigoAndIdNot(form.getCodigo(), id)) {
            throw new BusinessException("Ya existe otro medicamento registrado con ese código");
        }
        aplicarDatos(medicamento, form);
        return medicamentoRepository.save(medicamento);
    }

    @Override
    public void cambiarEstado(Long id) {
        Medicamento medicamento = obtenerPorId(id);
        medicamento.setEstado(medicamento.getEstado() == Estado.ACTIVO ? Estado.INACTIVO : Estado.ACTIVO);
        medicamentoRepository.save(medicamento);
    }

    private void aplicarDatos(Medicamento medicamento, MedicamentoForm form) {
        medicamento.setCodigo(form.getCodigo());
        medicamento.setNombre(form.getNombre());
        medicamento.setDescripcion(form.getDescripcion());
        medicamento.setPrecioVenta(form.getPrecioVenta());
        if (form.getCategoriaId() != null) {
            CategoriaMedicamento categoria = categoriaRepository.findById(form.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No se encontró la categoría con id " + form.getCategoriaId()));
            medicamento.setCategoria(categoria);
        } else {
            medicamento.setCategoria(null);
        }
    }
}