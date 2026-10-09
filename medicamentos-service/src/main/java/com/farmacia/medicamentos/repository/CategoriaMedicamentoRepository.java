package com.farmacia.medicamentos.repository;

import com.farmacia.medicamentos.entity.CategoriaMedicamento;
import com.farmacia.medicamentos.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaMedicamentoRepository extends JpaRepository<CategoriaMedicamento, Long> {

    List<CategoriaMedicamento> findAllByOrderByNombreAsc();

    List<CategoriaMedicamento> findByEstadoOrderByNombreAsc(Estado estado);

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
