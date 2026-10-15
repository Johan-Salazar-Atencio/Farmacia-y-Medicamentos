
package com.farmacia.core.auditoria;

import com.farmacia.core.entity.AccionAuditoria;
import com.farmacia.core.security.DatosSolicitud;
import com.farmacia.core.service.AuditoriaService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

@Aspect
@Component
public class AuditoriaAspect {

    private static final Logger log =
            LoggerFactory.getLogger(AuditoriaAspect.class);

    private static final Map<String, AccionAuditoria> ACCIONES =
            new LinkedHashMap<>();

    static {
        ACCIONES.put("crear", AccionAuditoria.CREAR);
        ACCIONES.put("registrar", AccionAuditoria.CREAR);
        ACCIONES.put("actualizar", AccionAuditoria.ACTUALIZAR);
        ACCIONES.put("cambiarEstado", AccionAuditoria.CAMBIO_ESTADO);
        ACCIONES.put("cambiarActivo", AccionAuditoria.CAMBIO_ESTADO);
        ACCIONES.put("cambiarPassword", AccionAuditoria.ACTUALIZAR);
        ACCIONES.put("anular", AccionAuditoria.ANULAR);
    }

    private final AuditoriaService auditoriaService;

    public AuditoriaAspect(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @Pointcut("execution(* com.farmacia.core.service.impl.*ServiceImpl.*(..))"
            + " && !within(com.farmacia.core.service.impl.AuditoriaServiceImpl)")
    public void serviciosDeNegocio() {
    }

    @Around("serviciosDeNegocio()")
    public Object auditar(ProceedingJoinPoint punto) throws Throwable {
        String nombreMetodo = punto.getSignature().getName();
        AccionAuditoria accion = ACCIONES.get(nombreMetodo);

        Object resultado = punto.proceed();

        if (accion == null) {
            return resultado;
        }

        try {
            auditoriaService.registrar(
                    accion,
                    DatosSolicitud.usuarioActual(),
                    punto.getTarget().getClass().getSimpleName(),
                    idEntidad(punto, resultado),
                    detalle(punto, nombreMetodo),
                    DatosSolicitud.ip(requestActual()),
                    DatosSolicitud.userAgent(requestActual())
            );
        } catch (Exception ex) {
            log.warn("No se pudo registrar auditoría: {}", ex.getMessage());
        }

        return resultado;
    }

    private Long idEntidad(ProceedingJoinPoint punto, Object resultado) {
        if (resultado != null) {
            try {
                Method metodo = resultado.getClass().getMethod("getId");
                Object id = metodo.invoke(resultado);

                if (id instanceof Long numero) {
                    return numero;
                }

                if (id instanceof Number numero) {
                    return numero.longValue();
                }
            } catch (Exception ignored) {
            }
        }

        for (Object argumento : punto.getArgs()) {
            if (argumento instanceof Long numero) {
                return numero;
            }
        }

        return null;
    }

    private String detalle(ProceedingJoinPoint punto, String metodo) {
        if ("cambiarPassword".equals(metodo)) {
            return "Contraseña actualizada (valor no registrado)";
        }

        StringBuilder detalle = new StringBuilder(metodo);

        for (Object argumento : punto.getArgs()) {
            if (argumento == null) {
                continue;
            }

            Package paquete = argumento.getClass().getPackage();

            if (paquete == null
                    || !paquete.getName().startsWith("com.farmacia.core.dto")) {
                continue;
            }

            detalle.append(" | ")
                    .append(resumen(argumento));
        }

        return detalle.length() > 1000
                ? detalle.substring(0, 1000)
                : detalle.toString();
    }

    private String resumen(Object dto) {
        StringBuilder salida =
                new StringBuilder(dto.getClass().getSimpleName()).append("{");

        boolean primero = true;

        for (Method metodo : dto.getClass().getMethods()) {
            String nombre = metodo.getName();

            if (!nombre.startsWith("get")
                    || metodo.getParameterCount() != 0
                    || nombre.equals("getClass")) {
                continue;
            }

            String propiedad = Character.toLowerCase(nombre.charAt(3))
                    + nombre.substring(4);

            if (propiedad.toLowerCase().contains("password")) {
                continue;
            }

            try {
                Object valor = metodo.invoke(dto);

                if (!primero) {
                    salida.append(", ");
                }

                salida.append(propiedad).append("=").append(valor);
                primero = false;
            } catch (Exception ignored) {
            }
        }

        return salida.append('}').toString();
    }

    private HttpServletRequest requestActual() {
        var atributos = RequestContextHolder.getRequestAttributes();

        return atributos instanceof ServletRequestAttributes servlet
                ? servlet.getRequest()
                : null;
    }
}
