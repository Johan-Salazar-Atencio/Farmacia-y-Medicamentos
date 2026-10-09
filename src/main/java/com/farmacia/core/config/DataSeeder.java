package com.farmacia.core.config;

import com.farmacia.core.entity.Rol;
import com.farmacia.core.entity.Usuario;
import com.farmacia.core.repository.RolRepository;
import com.farmacia.core.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Rol admin = crearRolSiNoExiste(
                "ADMIN", "Acceso total al sistema");

        Rol cajero = crearRolSiNoExiste(
                "CAJERO", "Operación de inventario y lotes");

        if (usuarioRepository.count() > 0) {
            return;
        }

        crearUsuario(
                "admin",
                "Administrador General",
                "admin@farmacia.pe",
                "Admin123*",
                Set.of(admin));

        crearUsuario(
                "cajero",
                "Cajero de Turno",
                "cajero@farmacia.pe",
                "Cajero123*",
                Set.of(cajero));
    }

    private Rol crearRolSiNoExiste(
            String nombre, String descripcion) {
        return rolRepository.findByNombre(nombre)
                .orElseGet(() ->
                        rolRepository.save(
                                new Rol(nombre, descripcion)));
    }

    private void crearUsuario(
            String username,
            String nombre,
            String email,
            String password,
            Set<Rol> roles) {

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRoles(new LinkedHashSet<>(roles));
        usuario.setActivo(true);

        usuarioRepository.save(usuario);
    }
}