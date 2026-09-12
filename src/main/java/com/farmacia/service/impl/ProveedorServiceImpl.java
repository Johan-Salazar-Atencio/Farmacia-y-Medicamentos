package com.farmacia.service.impl;

import com.farmacia.dto.ProveedorForm;
import com.farmacia.entity.Estado;
import com.farmacia.entity.Proveedor;
import com.farmacia.exception.BusinessException;
import com.farmacia.exception.ResourceNotFoundException;
import com.farmacia.repository.ProveedorRepository;
import com.farmacia.service.ProveedorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> listarTodos() {
        return proveedorRepository.findAllByOrderByRazonSocialAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> listarActivos() {
        return proveedorRepository.findByEstadoOrderByRazonSocialAsc(Estado.ACTIVO);
    }

    @Override
    @Transactional(readOnly = true)
    public Proveedor obtenerPorId(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el proveedor con id " + id));
    }

    @Override
    public Proveedor crear(ProveedorForm form) {
        if (proveedorRepository.existsByRuc(form.getRuc())) {
            throw new BusinessException("Ya existe un proveedor registrado con ese RUC");
        }
        Proveedor proveedor = new Proveedor();
        aplicarDatos(proveedor, form);
        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor actualizar(Long id, ProveedorForm form) {
        Proveedor proveedor = obtenerPorId(id);
        if (proveedorRepository.existsByRucAndIdNot(form.getRuc(), id)) {
            throw new BusinessException("Ya existe otro proveedor registrado con ese RUC");
        }
        aplicarDatos(proveedor, form);
        return proveedorRepository.save(proveedor);
    }

    @Override
    public void cambiarEstado(Long id) {
        Proveedor proveedor = obtenerPorId(id);
        proveedor.setEstado(proveedor.getEstado() == Estado.ACTIVO ? Estado.INACTIVO : Estado.ACTIVO);
        proveedorRepository.save(proveedor);
    }

    private void aplicarDatos(Proveedor proveedor, ProveedorForm form) {
        proveedor.setRuc(form.getRuc());
        proveedor.setRazonSocial(form.getRazonSocial());
        proveedor.setTelefono(form.getTelefono());
        proveedor.setEmail(form.getEmail());
        proveedor.setDireccion(form.getDireccion());
    }
}
