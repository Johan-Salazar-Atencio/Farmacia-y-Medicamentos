
package com.farmacia.core.service;

import com.farmacia.core.dto.UsuarioForm;
import com.farmacia.core.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsuarioService {

    List<Usuario> listarTodos();

    Page<Usuario> listarPaginado(Pageable pageable);

    Usuario obtenerPorId(Long id);

    Usuario crear(UsuarioForm form);

    Usuario actualizar(Long id, UsuarioForm form);

    void cambiarActivo(Long id);

    void cambiarPassword(Long id, String password, String confirmarPassword);

    boolean existeUsername(String username, Long idExcluido);

    boolean existeEmail(String email, Long idExcluido);
}
