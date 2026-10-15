
package com.farmacia.core.repository;

import com.farmacia.core.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    List<Usuario> findAllByOrderByUsernameAsc();

    @Query("SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.roles WHERE u.id = :id")
    Optional<Usuario> findByIdConRoles(@Param("id") Long id);

    @Query("SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.roles ORDER BY u.username ASC")
    List<Usuario> listarConRoles();
}
