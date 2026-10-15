
package com.farmacia.core.web;

import com.farmacia.core.security.DatosSolicitud;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SeguridadModelAdvice {

    @ModelAttribute("usuarioActual")
    public String usuarioActual() {
        return DatosSolicitud.usuarioActual();
    }

    @ModelAttribute("esAdmin")
    public boolean esAdmin() {
        Authentication autenticacion =
                SecurityContextHolder.getContext().getAuthentication();

        if (autenticacion == null) {
            return false;
        }

        return autenticacion.getAuthorities().stream()
                .anyMatch(autoridad ->
                        "ROLE_ADMIN".equals(autoridad.getAuthority()));
    }
}
