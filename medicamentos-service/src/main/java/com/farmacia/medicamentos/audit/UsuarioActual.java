package com.farmacia.medicamentos.audit;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Obtiene el usuario que ejecuta la operación. Mientras no se integre con auth-core
 * (JWT / Spring Security) se toma, en orden: header X-Usuario, parámetro ?usuario=,
 * cookie "usuario" (la fija el cuadro de la barra de navegación) o "sistema".
 * Al integrar seguridad solo hay que cambiar este método por SecurityContextHolder.
 */
@Component
public class UsuarioActual {

    public String obtener() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attrs) {
            HttpServletRequest req = attrs.getRequest();
            String u = req.getHeader("X-Usuario");
            if (esValido(u)) return recortar(u);
            u = req.getParameter("usuario");
            if (esValido(u)) return recortar(u);
            if (req.getCookies() != null) {
                for (Cookie c : req.getCookies()) {
                    if ("usuario".equals(c.getName()) && esValido(c.getValue())) return recortar(c.getValue());
                }
            }
            if (req.getUserPrincipal() != null) return recortar(req.getUserPrincipal().getName());
        }
        return "sistema";
    }

    private boolean esValido(String s) {
        return s != null && !s.isBlank();
    }

    private String recortar(String s) {
        String t = s.trim();
        return t.length() > 50 ? t.substring(0, 50) : t;
    }
}
