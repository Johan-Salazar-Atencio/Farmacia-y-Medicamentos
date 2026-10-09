package com.farmacia.medicamentos.audit;

import com.farmacia.medicamentos.service.AuditoriaService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * AOP (Programación Orientada a Aspectos): intercepta cualquier método anotado con
 * @Auditable DESPUÉS de que termine sin excepción y guarda una fila en la tabla
 * auditoria. Los servicios no tienen código de auditoría: está todo aquí.
 */
@Aspect
@Component
public class AuditoriaAspect {

    private final AuditoriaService auditoriaService;
    private final UsuarioActual usuarioActual;

    public AuditoriaAspect(AuditoriaService auditoriaService, UsuarioActual usuarioActual) {
        this.auditoriaService = auditoriaService;
        this.usuarioActual = usuarioActual;
    }

    @AfterReturning(pointcut = "@annotation(auditable)", returning = "resultado")
    public void registrar(JoinPoint jp, Auditable auditable, Object resultado) {
        Long id = extraerId(resultado, jp.getArgs());
        String detalle = jp.getSignature().getDeclaringType().getSimpleName()
                + "." + jp.getSignature().getName();
        auditoriaService.registrar(usuarioActual.obtener(), auditable.operacion(),
                auditable.entidad(), id, detalle);
    }

    /** Si el método devuelve la entidad usa su getId(); si es void usa el primer argumento Long. */
    private Long extraerId(Object resultado, Object[] args) {
        if (resultado != null) {
            try {
                Method m = resultado.getClass().getMethod("getId");
                Object v = m.invoke(resultado);
                if (v instanceof Long l) return l;
            } catch (ReflectiveOperationException ignorado) {
                // no tiene getId(): se intenta con los argumentos
            }
        }
        for (Object a : args) {
            if (a instanceof Long l) return l;
        }
        return null;
    }
}
