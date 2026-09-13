package com.farmacia.repository;

import com.farmacia.entity.Estado;
import com.farmacia.entity.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

    boolean existsByCodigo(String codigo);

    Optional<Medicamento> findTopByOrderByIdDesc();

    /**
     * RF-FAR-04 (listado con filtros): estado y categoría son opcionales (null = "todos").
     * JOIN FETCH m.categoria: carga la categoría en la misma consulta para evitar
     * LazyInitializationException al renderizar med.categoria.nombre en la vista,
     * ya que la sesión de Hibernate se cierra antes de que Thymeleaf procese la plantilla.
     */
    @Query("SELECT m FROM Medicamento m JOIN FETCH m.categoria WHERE (:estado IS NULL OR m.estado = :estado) " +
            "AND (:categoriaId IS NULL OR m.categoria.id = :categoriaId) ORDER BY m.nombre ASC")
    List<Medicamento> buscar(@Param("estado") Estado estado, @Param("categoriaId") Long categoriaId);

    /**
     * FIND ONE con JOIN FETCH: se usa en obtenerPorId() (servicio). La entidad puede
     * ser serializada fuera de la transacción (API REST con open-in-view=false), así
     * que la categoría debe venir ya cargada para evitar LazyInitializationException.
     */
    @Query("SELECT m FROM Medicamento m JOIN FETCH m.categoria WHERE m.id = :id")
    Optional<Medicamento> findByIdConCategoria(@Param("id") Long id);

    /**
     * JOIN FETCH aquí también: este método se usa en editarFormulario(), que ya accede a
     * medicamento.getCategoria().getId() dentro de la transacción, así que técnicamente
     * no lo necesitaba — pero mantenerlo consistente evita el mismo bug si en el futuro
     * se usa esta lista en una vista.
     */
    @Query("SELECT m FROM Medicamento m JOIN FETCH m.categoria WHERE m.estado = :estado ORDER BY m.nombre ASC")
    List<Medicamento> findByEstadoOrderByNombreAsc(@Param("estado") Estado estado);
}