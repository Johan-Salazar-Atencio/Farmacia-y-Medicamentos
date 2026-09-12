package com.farmacia.repository;

import com.farmacia.entity.Estado;
import com.farmacia.entity.LoteMedicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoteMedicamentoRepository extends JpaRepository<LoteMedicamento, Long> {

    /**
     * RF regla 4: listado principal de lotes = activos y NO vencidos.
     * JOIN FETCH l.medicamento y l.proveedor: evita LazyInitializationException en
     * lotes/list.html, que accede a lote.medicamento.codigo/nombre y lote.proveedor.razonSocial.
     */
    @Query("SELECT l FROM LoteMedicamento l " +
            "JOIN FETCH l.medicamento " +
            "JOIN FETCH l.proveedor " +
            "WHERE l.estado = :estado AND l.fechaVencimiento >= :fechaLimite " +
            "ORDER BY l.fechaVencimiento ASC")
    List<LoteMedicamento> findByEstadoAndFechaVencimientoGreaterThanEqualOrderByFechaVencimientoAsc(
            @Param("estado") Estado estado, @Param("fechaLimite") LocalDate fechaLimite);

    /**
     * RF-FAR-13: lotes activos cuya fecha de vencimiento cae dentro de un rango (alertas 30/60 días).
     * JOIN FETCH l.medicamento y l.proveedor: evita LazyInitializationException en alertas/list.html.
     */
    @Query("SELECT l FROM LoteMedicamento l " +
            "JOIN FETCH l.medicamento " +
            "JOIN FETCH l.proveedor " +
            "WHERE l.estado = :estado AND l.fechaVencimiento BETWEEN :desde AND :hasta " +
            "ORDER BY l.fechaVencimiento ASC")
    List<LoteMedicamento> findByEstadoAndFechaVencimientoBetweenOrderByFechaVencimientoAsc(
            @Param("estado") Estado estado, @Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    /**
     * Lotes ya vencidos (para la sección de alertas).
     * JOIN FETCH l.medicamento y l.proveedor: mismo motivo que los métodos anteriores.
     */
    @Query("SELECT l FROM LoteMedicamento l " +
            "JOIN FETCH l.medicamento " +
            "JOIN FETCH l.proveedor " +
            "WHERE l.estado = :estado AND l.fechaVencimiento < :hoy " +
            "ORDER BY l.fechaVencimiento DESC")
    List<LoteMedicamento> findByEstadoAndFechaVencimientoBeforeOrderByFechaVencimientoDesc(
            @Param("estado") Estado estado, @Param("hoy") LocalDate hoy);

    /**
     * Se mantiene con los mismos JOIN FETCH por consistencia, en caso de que su resultado
     * se use alguna vez directamente en una vista.
     */
    @Query("SELECT l FROM LoteMedicamento l " +
            "JOIN FETCH l.medicamento " +
            "JOIN FETCH l.proveedor " +
            "WHERE l.medicamento.id = :medicamentoId ORDER BY l.fechaVencimiento ASC")
    List<LoteMedicamento> findByMedicamentoIdOrderByFechaVencimientoAsc(@Param("medicamentoId") Long medicamentoId);
}
