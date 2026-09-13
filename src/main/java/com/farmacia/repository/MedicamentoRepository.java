package com.farmacia.repository;

import com.farmacia.entity.Estado;
import com.farmacia.entity.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

    boolean existsByCodigo(String codigo);
    boolean existsByCodigoAndIdNot(String codigo, Long id);
    List<Medicamento> findAllByOrderByNombreAsc();
    List<Medicamento> findByEstadoOrderByNombreAsc(Estado estado);
}