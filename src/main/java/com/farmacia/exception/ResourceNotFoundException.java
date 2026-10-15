package com.farmacia.exception;

/**
 * Se lanza cuando se busca por id una entidad que no existe (o fue eliminada lógicamente
 * y no debería ser referenciable, según el caso de uso).
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
