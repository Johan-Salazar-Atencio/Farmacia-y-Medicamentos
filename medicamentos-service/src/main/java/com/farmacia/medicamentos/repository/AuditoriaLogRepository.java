package com.farmacia.medicamentos.repository;

import com.farmacia.medicamentos.entity.AuditoriaLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuditoriaLogRepository extends JpaRepository<AuditoriaLog, Long> {

    @Query("SELECT a FROM AuditoriaLog a WHERE (:entidad IS NULL OR a.entidad = :entidad) " +
            "ORDER BY a.fechaHora DESC, a.id DESC")
    List<AuditoriaLog> buscar(@Param("entidad") String entidad);
}
