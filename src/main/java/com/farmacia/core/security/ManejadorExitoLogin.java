
package com.farmacia.core.security;

import com.farmacia.core.entity.AccionAuditoria;
import com.farmacia.core.repository.UsuarioRepository;
import com.farmacia.core.service.AuditoriaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ManejadorExitoLogin extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;
    private final AuditoriaService auditoriaService;

    public ManejadorExitoLogin(UsuarioRepository usuarioRepository,
                               AuditoriaService auditoriaService) {
        this.usuarioRepository = usuarioRepository;
        this.auditoriaService = auditoriaService;
        setDefaultTargetUrl("/medicamentos");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws jakarta.servlet.ServletException, java.io.IOException {
        String username = authentication.getName();

        try {
            usuarioRepository.findByUsername(username).ifPresent(usuario -> {
                usuario.setIntentosFallidos(0);
                usuario.setBloqueadoHasta(null);
                usuario.setUltimoAcceso(LocalDateTime.now());
                usuarioRepository.save(usuario);
            });
        } catch (Exception ignorado) {
        }

        auditoriaService.registrar(
                AccionAuditoria.LOGIN_OK,
                username,
                "Sesión",
                null,
                "Inicio de sesión correcto",
                DatosSolicitud.ip(request),
                DatosSolicitud.userAgent(request));

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
