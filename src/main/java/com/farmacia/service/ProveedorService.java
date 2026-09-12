package com.farmacia.service;

import com.farmacia.dto.ProveedorForm;
import com.farmacia.entity.Proveedor;

import java.util.List;

public interface ProveedorService {

    List<Proveedor> listarTodos();

    List<Proveedor> listarActivos();

    Proveedor obtenerPorId(Long id);

    Proveedor crear(ProveedorForm form);

    Proveedor actualizar(Long id, ProveedorForm form);

    void cambiarEstado(Long id);
}
