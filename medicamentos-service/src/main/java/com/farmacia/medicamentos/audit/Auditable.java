package com.farmacia.medicamentos.audit;

import java.lang.annotation.*;

/** Marca un método de servicio para que AuditoriaAspect registre la operación. */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {
    String entidad();
    Operacion operacion();
}
