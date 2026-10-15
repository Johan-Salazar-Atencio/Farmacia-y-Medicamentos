package com.farmacia.core.security;

import com.farmacia.core.entity.Usuario;
import com.farmacia.core.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class UsuarioUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "No existe el usuario " + username));

        return User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(usuario.getRoles().stream()
                        .map(rol -> new SimpleGrantedAuthority(
                                "ROLE_" + rol.getNombre()))
                        .collect(Collectors.toSet()))
                .disabled(!usuario.estaActivo())
                .accountLocked(usuario.estaBloqueado())
                .build();
    }
}
