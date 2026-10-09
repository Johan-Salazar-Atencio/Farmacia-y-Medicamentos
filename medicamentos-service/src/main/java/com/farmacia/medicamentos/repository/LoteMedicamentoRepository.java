package com.farmacia.medicamentos.repository;

import com.farmacia.medicamentos.entity.LoteMedicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LoteMedicamentoRepository extends JpaRepository<LoteMedicamento, Long> {

    /** JOIN FETCH: la vista usa lote.medicamento.nombre y open-in-view=false. */
    @Query("SELECT l FROM LoteMedicamento l JOIN FETCH l.medicamento m " +
            "WHERE (:medicamentoId IS NULL OR m.id = :medicamentoId) ORDER BY l.fechaVencimiento ASC")
    List<LoteMedicamento> buscar(@Param("medicamentoId") Long medicamentoId);

    @Query("SELECT l FROM LoteMedicamento l JOIN FETCH l.medicamento WHERE l.id = :id")
    Optional<LoteMedicamento> findByIdConMedicamento(@Param("id") Long id);

    boolean existsByMedicamentoIdAndNumeroLoteIgnoreCase(Long medicamentoId, String numeroLote);
}
