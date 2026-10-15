package com.farmacia.service.impl;

import com.farmacia.dto.CategoriaMedicamentoForm;
import com.farmacia.entity.CategoriaMedicamento;
import com.farmacia.entity.Estado;
import com.farmacia.exception.BusinessException;
import com.farmacia.exception.ResourceNotFoundException;
import com.farmacia.repository.CategoriaMedicamentoRepository;
import com.farmacia.service.CategoriaMedicamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoriaMedicamentoServiceImpl implements CategoriaMedicamentoService {

    private final CategoriaMedicamentoRepository categoriaRepository;

    public CategoriaMedicamentoServiceImpl(CategoriaMedicamentoRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaMedicamento> listarTodas() {
        return categoriaRepository.findAllByOrderByNombreAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaMedicamento> listarActivas() {
        return categoriaRepository.findByEstadoOrderByNombreAsc(Estado.ACTIVO);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaMedicamento obtenerPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría con id " + id));
    }

    @Override
    public CategoriaMedicamento crear(CategoriaMedicamentoForm form) {
        if (categoriaRepository.existsByNombreIgnoreCase(form.getNombre())) {
            throw new BusinessException("Ya existe una categoría registrada con ese nombre");
        }
        CategoriaMedicamento categoria = new CategoriaMedicamento();
        categoria.setNombre(form.getNombre());
        categoria.setDescripcion(form.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    @Override
    public CategoriaMedicamento actualizar(Long id, CategoriaMedicamentoForm form) {
        CategoriaMedicamento categoria = obtenerPorId(id);
        if (categoriaRepository.existsByNombreIgnoreCaseAndIdNot(form.getNombre(), id)) {
            throw new BusinessException("Ya existe otra categoría registrada con ese nombre");
        }
        categoria.setNombre(form.getNombre());
        categoria.setDescripcion(form.getDescripcion());
        return categoriaRepository.save(categoria);
    }

    @Override
    public void cambiarEstado(Long id) {
        CategoriaMedicamento categoria = obtenerPorId(id);
        categoria.setEstado(categoria.getEstado() == Estado.ACTIVO ? Estado.INACTIVO : Estado.ACTIVO);
        categoriaRepository.save(categoria);
    }
}
