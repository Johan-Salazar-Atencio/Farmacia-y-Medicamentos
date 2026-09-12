package com.farmacia.exception;

/**
 * Se lanza cuando se viola una regla de negocio (código/RUC/nombre duplicado,
 * fecha de vencimiento inválida, etc). El mensaje va en español porque se muestra
 * directamente al usuario final.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String mensaje) {
        super(mensaje);
    }
}
