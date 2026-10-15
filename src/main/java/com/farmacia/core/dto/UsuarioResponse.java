
package com.farmacia.core.dto;

import com.farmacia.core.entity.Usuario;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record UsuarioResponse(
        Long id,
        String username,
        String nombre,
        String email,
        Boolean activo,
        Set<String> roles,
        LocalDateTime fechaRegistro,
        LocalDateTime ultimoAcceso
) {

    public static UsuarioResponse desde(Usuario usuario) {
        Set<String> nombresRoles = usuario.getRoles() == null
                ? new LinkedHashSet<>()
                : usuario.getRoles().stream()
                .map(rol -> rol.getNombre())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getActivo(),
                nombresRoles,
                usuario.getFechaRegistro(),
                usuario.getUltimoAcceso()
        );
    }
}
