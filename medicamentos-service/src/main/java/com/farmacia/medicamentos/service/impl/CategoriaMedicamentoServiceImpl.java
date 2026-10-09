package com.farmacia.medicamentos.service.impl;

import com.farmacia.medicamentos.audit.Auditable;
import com.farmacia.medicamentos.audit.Operacion;
import com.farmacia.medicamentos.dto.CategoriaForm;
import com.farmacia.medicamentos.entity.CategoriaMedicamento;
import com.farmacia.medicamentos.entity.Estado;
import com.farmacia.medicamentos.exception.BusinessException;
import com.farmacia.medicamentos.exception.ResourceNotFoundException;
import com.farmacia.medicamentos.repository.CategoriaMedicamentoRepository;
import com.farmacia.medicamentos.service.CategoriaMedicamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoriaMedicamentoServiceImpl implements CategoriaMedicamentoService {

    private final CategoriaMedicamentoRepository repository;

    public CategoriaMedicamentoServiceImpl(CategoriaMedicamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaMedicamento> listarTodas() {
        return repository.findAllByOrderByNombreAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaMedicamento> listarActivas() {
        return repository.findByEstadoOrderByNombreAsc(Estado.ACTIVO);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaMedicamento obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría con id " + id));
    }

    @Override
    @Auditable(entidad = "CategoriaMedicamento", operacion = Operacion.REGISTRO)
    public CategoriaMedicamento crear(CategoriaForm form) {
        if (repository.existsByNombreIgnoreCase(form.getNombre().trim())) {
            throw new BusinessException("Ya existe una categoría con el nombre '" + form.getNombre().trim() + "'");
        }
        CategoriaMedicamento c = new CategoriaMedicamento();
        c.setNombre(form.getNombre().trim());
        c.setDescripcion(form.getDescripcion());
        return repository.save(c);
    }

    @Override
    @Auditable(entidad = "CategoriaMedicamento", operacion = Operacion.MODIFICACION)
    public CategoriaMedicamento actualizar(Long id, CategoriaForm form) {
        CategoriaMedicamento c = obtenerPorId(id);
        if (repository.existsByNombreIgnoreCaseAndIdNot(form.getNombre().trim(), id)) {
            throw new BusinessException("Ya existe una categoría con el nombre '" + form.getNombre().trim() + "'");
        }
        c.setNombre(form.getNombre().trim());
        c.setDescripcion(form.getDescripcion());
        return repository.save(c);
    }

    @Override
    @Auditable(entidad = "CategoriaMedicamento", operacion = Operacion.CAMBIO_ESTADO)
    public void cambiarEstado(Long id) {
        CategoriaMedicamento c = obtenerPorId(id);
        c.setEstado(c.getEstado() == Estado.ACTIVO ? Estado.INACTIVO : Estado.ACTIVO);
        repository.save(c);
    }
}
