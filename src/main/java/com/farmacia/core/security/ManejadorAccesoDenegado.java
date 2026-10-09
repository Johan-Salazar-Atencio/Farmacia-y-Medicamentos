
package com.farmacia.core.security;

import com.farmacia.core.entity.AccionAuditoria;
import com.farmacia.core.service.AuditoriaService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class ManejadorAccesoDenegado implements AccessDeniedHandler {

    public static final String RUTA_403 = "/acceso-denegado";

    private final AuditoriaService auditoriaService;

    public ManejadorAccesoDenegado(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) {
        auditoriaService.registrar(
                AccionAuditoria.ACCESO_DENEGADO,
                DatosSolicitud.usuarioActual(),
                "Seguridad",
                null,
                request.getMethod() + " " + request.getRequestURI()
                        + " - " + accessDeniedException.getMessage(),
                DatosSolicitud.ip(request),
                DatosSolicitud.userAgent(request));

        String uri = request.getRequestURI();

        if (uri != null && uri.startsWith(request.getContextPath() + "/api")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");

            try {
                response.getWriter().write(
                        "{\"error\":\"Acceso denegado\",\"detalle\":\""
                                + escapanJson(accessDeniedException.getMessage())
                                + "\"}");
            } catch (Exception ignorado) {
            }
            return;
        }

        try {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            RequestDispatcher dispatcher =
                    request.getRequestDispatcher(RUTA_403);
            dispatcher.forward(request, response);
        } catch (Exception ignorado) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private String escapanJson(String valor) {
        if (valor == null) {
            return "";
        }

        return valor.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", " ");
    }
}
