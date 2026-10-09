
package com.farmacia.core.security;

import com.farmacia.core.entity.AccionAuditoria;
import com.farmacia.core.service.AuditoriaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class ManejadorLogout implements LogoutHandler {

    private final AuditoriaService auditoriaService;

    public ManejadorLogout(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String username = authentication != null
                ? authentication.getName()
                : DatosSolicitud.usuarioActual();

        auditoriaService.registrar(
                AccionAuditoria.LOGOUT,
                username,
                "Sesión",
                null,
                "Cierre de sesión",
                DatosSolicitud.ip(request),
                DatosSolicitud.userAgent(request));

        SecurityContextHolder.clearContext();
    }
}
