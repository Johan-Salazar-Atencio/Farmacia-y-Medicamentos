package com.farmacia.core.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class DatosSolicitud {

    private DatosSolicitud() {
    }

    public static String usuarioActual() {
        Authentication autenticacion =
                SecurityContextHolder.getContext().getAuthentication();

        if (autenticacion == null || !autenticacion.isAuthenticated()
                || "anonymousUser".equals(autenticacion.getPrincipal())) {
            return null;
        }

        return autenticacion.getName();
    }

    public static String ip(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        String cabecera = request.getHeader("X-Forwarded-For");

        if (cabecera != null && !cabecera.isBlank()) {
            return cabecera.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    public static String userAgent(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        return request.getHeader("User-Agent");
    }
}