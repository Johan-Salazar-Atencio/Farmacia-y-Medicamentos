
package com.farmacia.core.service.impl;

import com.farmacia.core.dto.UsuarioForm;
import com.farmacia.core.entity.Rol;
import com.farmacia.core.entity.Usuario;
import com.farmacia.core.exception.BusinessException;
import com.farmacia.core.exception.ResourceNotFoundException;
import com.farmacia.core.repository.RolRepository;
import com.farmacia.core.repository.UsuarioRepository;
import com.farmacia.core.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.listarConRoles();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> listarPaginado(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findByIdConRoles(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró el usuario con id " + id));
    }

    @Override
    public Usuario crear(UsuarioForm form) {
        validarUnicidad(form, null);

        if (form.getPassword() == null || form.getPassword().isBlank()) {
            throw new BusinessException(
                    "La contraseña es obligatoria al crear un usuario");
        }

        if (!form.getPassword().equals(form.getConfirmarPassword())) {
            throw new BusinessException(
                    "La contraseña y su confirmación no coinciden");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(form.getUsername().trim());
        usuario.setNombre(form.getNombre().trim());
        usuario.setEmail(form.getEmail().trim().toLowerCase());
        usuario.setPassword(passwordEncoder.encode(form.getPassword()));
        usuario.setActivo(form.getActivo() == null || form.getActivo());
        usuario.setRoles(resolverRoles(form.getRoles()));

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizar(Long id, UsuarioForm form) {
        Usuario usuario = obtenerPorId(id);
        validarUnicidad(form, id);

        boolean propio = esUsuarioActual(usuario.getUsername());

        if (propio && !form.getRoles().equals(nombresDe(usuario))) {
            throw new BusinessException(
                    "No puedes modificar tus propios roles");
        }

        usuario.setUsername(form.getUsername().trim());
        usuario.setNombre(form.getNombre().trim());
        usuario.setEmail(form.getEmail().trim().toLowerCase());
        usuario.setRoles(resolverRoles(form.getRoles()));
        usuario.setActivo(form.getActivo() == null || form.getActivo());

        if (form.getPassword() != null && !form.getPassword().isBlank()) {
            if (!form.getPassword().equals(form.getConfirmarPassword())) {
                throw new BusinessException(
                        "La contraseña y su confirmación no coinciden");
            }

            usuario.setPassword(passwordEncoder.encode(form.getPassword()));
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public void cambiarActivo(Long id) {
        Usuario usuario = obtenerPorId(id);

        if (esUsuarioActual(usuario.getUsername()) && usuario.estaActivo()) {
            throw new BusinessException(
                    "No puedes desactivar tu propia cuenta");
        }

        usuario.setActivo(!usuario.estaActivo());
        usuarioRepository.save(usuario);
    }

    @Override
    public void cambiarPassword(
            Long id, String password, String confirmarPassword) {
        Usuario usuario = obtenerPorId(id);

        if (password == null || password.isBlank()) {
            throw new BusinessException("La contraseña es obligatoria");
        }

        if (!password.equals(confirmarPassword)) {
            throw new BusinessException(
                    "La contraseña y su confirmación no coinciden");
        }

        usuario.setPassword(passwordEncoder.encode(password));
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeUsername(String username, Long idExcluido) {
        return idExcluido == null
                ? usuarioRepository.existsByUsername(username)
                : usuarioRepository.existsByUsernameAndIdNot(username, idExcluido);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeEmail(String email, Long idExcluido) {
        return idExcluido == null
                ? usuarioRepository.existsByEmail(email)
                : usuarioRepository.existsByEmailAndIdNot(email, idExcluido);
    }

    private void validarUnicidad(UsuarioForm form, Long idExcluido) {
        String username = form.getUsername().trim();
        String email = form.getEmail().trim().toLowerCase();

        if (existeUsername(username, idExcluido)) {
            throw new BusinessException(
                    "Ya existe un usuario con ese nombre de usuario");
        }

        if (existeEmail(email, idExcluido)) {
            throw new BusinessException(
                    "Ya existe un usuario con ese correo");
        }
    }

    private Set<Rol> resolverRoles(Set<String> nombres) {
        Set<Rol> roles = new LinkedHashSet<>();

        if (nombres != null) {
            for (String nombre : nombres) {
                Rol rol = rolRepository.findByNombre(nombre)
                        .orElseThrow(() ->
                                new BusinessException(
                                        "El rol " + nombre + " no existe"));
                roles.add(rol);
            }
        }

        if (roles.isEmpty()) {
            throw new BusinessException("Debe asignar al menos un rol");
        }

        return roles;
    }

    private Set<String> nombresDe(Usuario usuario) {
        if (usuario.getRoles() == null) {
            return new LinkedHashSet<>();
        }

        return usuario.getRoles().stream()
                .map(Rol::getNombre)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private boolean esUsuarioActual(String username) {
        var autenticacion =
                SecurityContextHolder.getContext().getAuthentication();

        return autenticacion != null
                && username != null
                && username.equals(autenticacion.getName());
    }
}
