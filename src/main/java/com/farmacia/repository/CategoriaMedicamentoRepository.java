package com.farmacia.repository;

import com.farmacia.entity.CategoriaMedicamento;
import com.farmacia.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaMedicamentoRepository extends JpaRepository<CategoriaMedicamento, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);

    List<CategoriaMedicamento> findAllByOrderByNombreAsc();

    List<CategoriaMedicamento> findByEstadoOrderByNombreAsc(Estado estado);
}
