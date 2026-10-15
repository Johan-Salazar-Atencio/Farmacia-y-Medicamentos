
package com.farmacia.core.security;

import com.farmacia.core.entity.AccionAuditoria;
import com.farmacia.core.entity.Usuario;
import com.farmacia.core.repository.UsuarioRepository;
import com.farmacia.core.service.AuditoriaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ManejadorFalloLogin implements AuthenticationFailureHandler {

    private final UsuarioRepository usuarioRepository;
    private final AuditoriaService auditoriaService;

    public ManejadorFalloLogin(UsuarioRepository usuarioRepository,
                               AuditoriaService auditoriaService) {
        this.usuarioRepository = usuarioRepository;
        this.auditoriaService = auditoriaService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws java.io.IOException {
        String username = request.getParameter("username");
        AccionAuditoria accion = AccionAuditoria.LOGIN_FALLIDO;
        String detalle = exception.getMessage();

        if (exception instanceof LockedException) {
            accion = AccionAuditoria.LOGIN_BLOQUEADO;
        } else if (exception instanceof BadCredentialsException || username == null) {
            penalizar(username);
        }

        auditoriaService.registrar(
                accion,
                username,
                "Sesión",
                null,
                detalle,
                DatosSolicitud.ip(request),
                DatosSolicitud.userAgent(request));

        response.sendRedirect(request.getContextPath() + "/login?error="
                + (accion == AccionAuditoria.LOGIN_BLOQUEADO
                ? "bloqueado" : "credenciales"));
    }

    private void penalizar(String username) {
        if (username == null || username.isBlank()) {
            return;
        }

        try {
            usuarioRepository.findByUsername(username).ifPresent(usuario -> {
                LocalDateTime ahora = LocalDateTime.now();

                if (usuario.getBloqueadoHasta() != null
                        && usuario.getBloqueadoHasta().isBefore(ahora)) {
                    usuario.setBloqueadoHasta(null);
                    usuario.setIntentosFallidos(0);
                }

                int intentos = usuario.getIntentosFallidos() == null
                        ? 0 : usuario.getIntentosFallidos();
                intentos++;

                if (intentos >= Usuario.MAX_INTENTOS) {
                    usuario.setIntentosFallidos(Usuario.MAX_INTENTOS);
                    usuario.setBloqueadoHasta(
                            ahora.plusMinutes(Usuario.MINUTOS_BLOQUEO));
                } else {
                    usuario.setIntentosFallidos(intentos);
                }

                usuarioRepository.save(usuario);
            });
        } catch (Exception ignorado) {
        }
    }
}
