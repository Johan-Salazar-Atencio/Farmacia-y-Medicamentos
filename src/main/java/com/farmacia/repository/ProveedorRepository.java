package com.farmacia.repository;

import com.farmacia.entity.Estado;
import com.farmacia.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    boolean existsByRucIgnoreCase(String ruc);

    boolean existsByRucIgnoreCaseAndIdNot(String ruc, Long id);

    List<Proveedor> findAllByOrderByRazonSocialAsc();

    List<Proveedor> findByEstadoOrderByRazonSocialAsc(Estado estado);
}
